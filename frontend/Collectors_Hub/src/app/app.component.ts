import { Component, inject, OnInit, PLATFORM_ID } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavbarComponent } from './components/navbar/navbar.component';
import {
  CommonModule,
  isPlatformBrowser,
  isPlatformServer,
} from '@angular/common';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, NavbarComponent, CommonModule],
  templateUrl: './app.component.html',
})
export class AppComponent implements OnInit {
  title = 'Collectors_Hub';

  private authService = inject(AuthService);
  private platformId = inject(PLATFORM_ID);

  ngOnInit() {
    // Wywołujemy checkAuthStatus, ale AuthService sam zadecyduje,
    // czy potrzebne jest rzeczywiste odpytanie API
    this.authService.checkAuthStatus().subscribe();
  }
}
