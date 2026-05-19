import { User } from './../../../../../shared/interfaces/User';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../../../../core/service/user.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-user-edit',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './userEdit.component.html',
  styleUrls: ['./userEdit.component.css']
})
export class UserEditComponent implements OnInit {
  editableUser: User | null = null;
  errorMessage: string | null = null;
  selectedFile: File | null = null;
  imagePreview: string | ArrayBuffer | null = null;

  constructor(
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const userId = this.route.snapshot.paramMap.get('id');
    if (userId) {
      this.userService.getUserById(+userId).subscribe({
        next: (user) => {
          this.editableUser = user;
          if (user.photo) {
            this.imagePreview = 'http://localhost:8080/uploads/' + user.photo;
          } else {
            this.imagePreview = '/assets/images/default-user.png'; // Imagen por defecto
          }
        },
        error: (err) => this.errorMessage = 'No se pudo encontrar el usuario.'
      });
    }
  }

  onFileSelected(event: any): void {
    const file = event.target.files?.[0];
    if (file) {
      this.selectedFile = file;
      const reader = new FileReader();
      reader.onload = () => { this.imagePreview = reader.result; };
      reader.readAsDataURL(file);
    }
  }

  onSubmit(): void {
    if (!this.editableUser) return;


    const userUpdateData = {
      name: this.editableUser.name,
      surname: this.editableUser.surname,
      address: this.editableUser.address,
      phone: this.editableUser.phone,
      role: this.editableUser.role
    };

    const formData = new FormData();
    formData.append('user', new Blob([JSON.stringify(userUpdateData)], { type: 'application/json' }));

    if (this.selectedFile) {
      formData.append('file', this.selectedFile, this.selectedFile.name);
    }

    this.userService.updateUser(this.editableUser.userId, formData).subscribe({
      next: () => {
        Swal.fire('¡Actualizado!', 'El usuario ha sido modificado.', 'success')
          .then(() => this.router.navigate(['/profile/admin/users/list']));
      },
      error: (err) => {
        console.error("Error al actualizar:", err);
        this.errorMessage = 'Error al actualizar el usuario.';
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/profile/admin/users/list']);
  }
}
