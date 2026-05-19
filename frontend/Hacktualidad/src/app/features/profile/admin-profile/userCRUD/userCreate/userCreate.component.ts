import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import Swal from 'sweetalert2';
import { UserService } from '../../../../../core/service/user.service';
import { AdminUserCreateRequest } from '../../../../../shared/interfaces/userRegisterInAdmin';

@Component({
  selector: 'app-user-create',
  standalone: true,
  imports: [ CommonModule, FormsModule, RouterModule ],
  templateUrl: './userCreate.component.html',
  styleUrls: ['./userCreate.component.css']
})
export class UserCreateComponent {

  public user: AdminUserCreateRequest = {
    name: '',
    surname: '',
    email: '',
    password: '',
    confirmPassword: '',
    address: '',
    phone: null,
    photo: '',
    role: 'USER'
  };

  public selectedFile: File | null = null;
  public imagePreview: string | ArrayBuffer | null = null;

  constructor(
    private userService: UserService,
    private router: Router
  ) {}

  onFileSelected(event: any): void {
    const file = (event.target as HTMLInputElement)?.files?.[0];
    if (file) {
      this.selectedFile = file;
      const reader = new FileReader();
      reader.onload = () => { this.imagePreview = reader.result; };
      reader.readAsDataURL(file);
    }
  }

  onSubmit(): void {
    if (this.user.password !== this.user.confirmPassword) {
      Swal.fire('Error', 'Las contraseñas no coinciden.', 'error');
      return;
    }

    const formData = new FormData();
    formData.append('user', new Blob([JSON.stringify(this.user)], { type: 'application/json' }));

    if (this.selectedFile) {
      formData.append('file', this.selectedFile, this.selectedFile.name);
    }

    this.userService.createUserWithPhoto(formData).subscribe({
      next: () => {
        Swal.fire({
          title: '¡Usuario Creado!',
          text: 'El nuevo usuario ha sido añadido con éxito.',
          icon: 'success'
        }).then(() => this.router.navigate(['/profile/admin/users/list']));
      },
      error: (err) => {
        Swal.fire('Error', err.error.message || 'No se pudo crear el usuario.', 'error');
      }
    });
  }
}
