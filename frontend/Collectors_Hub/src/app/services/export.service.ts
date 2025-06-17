import { Injectable } from '@angular/core';
import { Item } from './item.service';
import * as XLSX from 'xlsx';
import { jsPDF } from 'jspdf';
import autoTable from 'jspdf-autotable';

@Injectable({
  providedIn: 'root',
})
export class ExportService {
  constructor() {}

  /**
   * Export items to CSV
   */
  exportToCsv(items: Item[], fileName: string = 'items.csv'): void {
    if (!items || !items.length) {
      return;
    }

    const separator = ',';
    const keys = ['id', 'name', 'description', 'categoryName'];

    // Get all possible attribute keys from all items
    const attributeKeys: string[] = [];
    items.forEach((item) => {
      if (item.attributes) {
        Object.keys(item.attributes).forEach((key) => {
          if (!attributeKeys.includes(key)) {
            attributeKeys.push(key);
          }
        });
      }
    });

    // Create header row
    let csvContent = keys.join(separator);
    if (attributeKeys.length) {
      csvContent +=
        separator +
        attributeKeys.map((key) => `attribute_${key}`).join(separator);
    }
    csvContent += '\n';

    // Add data rows
    items.forEach((item) => {
      // Add basic item properties
      const row: string[] = keys.map((key) => {
        const value = (item as any)[key];
        return value !== undefined && value !== null ? String(value) : '';
      });

      // Add attribute values
      if (attributeKeys.length) {
        attributeKeys.forEach((attrKey) => {
          const attrValue =
            item.attributes && item.attributes[attrKey] !== undefined
              ? item.attributes[attrKey]
              : '';
          row.push(String(attrValue));
        });
      }

      // Convert row to CSV format
      csvContent += row
        .map((value) => {
          // Escape quotes and wrap in quotes if contains special characters
          if (
            value.includes('"') ||
            value.includes(',') ||
            value.includes('\n')
          ) {
            return `"${value.replace(/"/g, '""')}"`;
          }
          return value;
        })
        .join(separator);
      csvContent += '\n';
    });

    // Create download link
    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
    const link = document.createElement('a');
    const url = URL.createObjectURL(blob);

    link.setAttribute('href', url);
    link.setAttribute('download', fileName);
    link.style.visibility = 'hidden';

    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }

  /**
   * Export items to Excel (XLSX format) using xlsx library
   */
  exportToExcel(items: Item[], fileName: string = 'items.xlsx'): void {
    if (!items || !items.length) {
      return;
    }

    const worksheet: XLSX.WorkSheet = XLSX.utils.json_to_sheet(
      this.prepareDataForExport(items)
    );

    // Set column widths
    const colWidths = [
      { wch: 5 }, // ID
      { wch: 20 }, // Name
      { wch: 30 }, // Description
      { wch: 15 }, // Category
    ];

    worksheet['!cols'] = colWidths;

    const workbook: XLSX.WorkBook = {
      Sheets: { Items: worksheet },
      SheetNames: ['Items'],
    };

    // Generate Excel file and trigger download
    XLSX.writeFile(workbook, fileName);
  }

  /**
   * Export items to PDF format using jsPDF
   */
  exportToPdf(items: Item[], fileName: string = 'items.pdf'): void {
    if (!items || !items.length) {
      return;
    }

    const doc = new jsPDF();

    // Add title
    doc.setFontSize(18);
    doc.text('Items Collection', 14, 22);
    doc.setFontSize(11);
    doc.text(`Exported on: ${new Date().toLocaleString()}`, 14, 30);

    const preparedData = this.prepareDataForExport(items);
    const headers = Object.keys(preparedData[0]);

    // Convert data to format expected by autoTable
    const tableData = preparedData.map((item) =>
      Object.values(item)
    ) as string[][];

    // Używamy autoTable jako importowanej funkcji
    autoTable(doc, {
      head: [headers],
      body: tableData,
      startY: 35,
      styles: { fontSize: 10, cellPadding: 3 },
      headStyles: { fillColor: [66, 66, 255] },
    });

    doc.save(fileName);
  }

  /**
   * Prepare data for export to Excel or PDF
   */
  private prepareDataForExport(items: Item[]): any[] {
    // Get all possible attribute keys from all items
    const attributeKeys: string[] = [];
    items.forEach((item) => {
      if (item.attributes) {
        Object.keys(item.attributes).forEach((key) => {
          if (!attributeKeys.includes(key)) {
            attributeKeys.push(key);
          }
        });
      }
    });

    return items.map((item) => {
      const exportItem: any = {
        ID: item.id,
        Name: item.name,
        Description: item.description || '',
        Category: item.categoryName || '',
      };

      // Add attributes as columns
      if (attributeKeys.length && item.attributes) {
        attributeKeys.forEach((key) => {
          exportItem[`Attribute_${key}`] =
            item.attributes && item.attributes[key] ? item.attributes[key] : '';
        });
      }

      return exportItem;
    });
  }
}
