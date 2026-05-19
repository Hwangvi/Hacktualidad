import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { UserRegister } from '../../shared/interfaces/UserRegister';
import { UserService } from '../../core/service/user.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  imports: [CommonModule, FormsModule, RouterModule]
})
export class RegisterComponent implements OnInit {

  public user: UserRegister = {
    name: '',
    surname: '',
    address: '',
    phone: null,
    photo: '',
    email: '',
    password: '',
    confirmPassword: ''
  };

  public selectedFile: File | null = null;
  public imagePreview: string | ArrayBuffer | null = null;

  constructor(
    private router: Router,
    private userService: UserService
  ) { }

  ngOnInit() {
  }

  onFileSelected(event: any): void {
    if (event.target.files && event.target.files[0]) {
      const file = event.target.files[0];
      this.selectedFile = file;

      const reader = new FileReader();
      reader.onload = () => {
        this.imagePreview = reader.result;
      };
      reader.readAsDataURL(file);
    }
  }

  public onSubmit(): void {
    if (this.user.password !== this.user.confirmPassword) {
      this.showErrorAlert('ERROR DE CRIPTOGRAFÍA: Las contraseñas no coinciden.');
      return;
    }

    const formData = new FormData();
    formData.append('user', JSON.stringify(this.user));

    if (this.selectedFile) {
      formData.append('file', this.selectedFile, this.selectedFile.name);
    }

    this.userService.register(formData).subscribe({
      next: (response) => {
        this.showSuccessAlert();
      },
      error: (err) => {
        const errorMessage = err.error || 'Fallo en la conexión con el servidor.';
        this.showErrorAlert(errorMessage);
      }
    });
  }

  showSuccessAlert() {
    Swal.fire({
      title: '¡ACCESO CONCEDIDO!',
      text: 'Usuario registrado correctamente en la base de datos.',
      icon: 'success',
      timer: 3500,
      timerProgressBar: true,
      background: '#0a0a0a',
      color: '#00ff00',
      confirmButtonText: '[ INICIAR PROTOCOLO LOGIN ]',
      confirmButtonColor: '#00ff00',
      iconColor: '#00ff00'
    }).then((result) => {
      this.router.navigate(['/login']);
    });
  }

  showErrorAlert(message: string) {
    Swal.fire({
      title: 'ERROR DE SISTEMA',
      text: message,
      icon: 'error',
      background: '#0a0a0a',
      color: '#ff3333',
      confirmButtonText: '[ REINTENTAR ]',
      confirmButtonColor: '#ff3333',
      iconColor: '#ff3333'
    });
  }
}
