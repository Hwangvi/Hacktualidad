import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../../core/service/auth.service';
import { User } from '../../../shared/interfaces/User';
import Swal from 'sweetalert2';
import { CommonModule } from '@angular/common';
import { AdminPanelComponent } from './admin-panel/admin-panel.component';

@Component({
  selector: 'app-admin-profile',
  templateUrl: './admin-profile.component.html',
  styleUrls: ['./admin-profile.component.css'],
  imports: [CommonModule, AdminPanelComponent]
})
export class AdminProfileComponent implements OnInit {

  userData: User | null = null;

  constructor(
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.authService.currentUser$.subscribe(user => {
      this.userData = user;
    });

    this.route.queryParams.subscribe(params => {
      if (params['updated'] === 'true') {
        this.showSuccessAlert();

        this.router.navigate([], {
          relativeTo: this.route,
          queryParams: { updated: null },
          queryParamsHandling: 'merge',
          replaceUrl: true
        });
      }
    });
  }

  showSuccessAlert() {
    Swal.fire({
      title: 'ACTUALIZACIÓN COMPLETADA',
      text: 'Tus datos han sido sincronizados en la base de datos.',
      icon: 'success',
      timer: 3000,
      timerProgressBar: true,
      background: '#0a0a0a',
      color: '#00ff00',
      confirmButtonText: '[ CERRAR ]',
      customClass: {
        popup: 'hacker-popup',
        title: 'hacker-title',
        confirmButton: 'hacker-confirm-button'
      }
    });
  }

  onModifyData(): void {
    this.router.navigate(['/profile/edit']);
  }
}
