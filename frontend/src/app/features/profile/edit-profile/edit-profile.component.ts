import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../core/service/auth.service';
import { UserService } from '../../../core/service/user.service';
import { User } from '../../../shared/interfaces/User';
import { environment } from '../../../../environments/environment';


@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css'],
  imports: [CommonModule, FormsModule],
})
export class EditProfileComponent implements OnInit {

  editableUser: User | null = null;
  errorMessage: string | null = null;

  public selectedFile: File | null = null;
  public imagePreview: string | ArrayBuffer | null = null;
  public uploadsUrl = environment.uploadsUrl;

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const currentUser = this.authService.currentUserValue;
    if (currentUser) {
      this.editableUser = { ...currentUser };
      if (this.editableUser.photo) {
        this.imagePreview = environment.uploadsUrl + this.editableUser.photo;
      }
    } else {
      this.router.navigate(['/login']);
    }
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


  onSubmit(): void {
    if (!this.editableUser) return;
    this.errorMessage = null;

    const userUpdateData = {
      name: this.editableUser.name,
      surname: this.editableUser.surname,
      address: this.editableUser.address,
      phone: this.editableUser.phone,
    };

    const formData = new FormData();
    formData.append('user', JSON.stringify(userUpdateData));

    if (this.selectedFile) {
      formData.append('file', this.selectedFile, this.selectedFile.name);
    }

    this.userService.updateUser(this.editableUser.userId, formData).subscribe({
      next: (updatedUser: User) => {
        this.authService.updateCurrentUser(updatedUser);

        if (updatedUser.role === 'ADMIN') {
          this.router.navigate(['/profile/admin'], { queryParams: { updated: 'true' } });
        } else {
          this.router.navigate(['/profile/user'], { queryParams: { updated: 'true' } });
        }
      },
      error: (err) => {
        console.error('Error al actualizar:', err);
        this.errorMessage = 'No se pudo actualizar el perfil. Inténtalo de nuevo.';
      }
    });
  }

  onCancel(): void {
    const currentUser = this.authService.currentUserValue;

    if (currentUser) {
      if (currentUser.role === 'ADMIN') {
        this.router.navigate(['/profile/admin']);
      } else {
        this.router.navigate(['/profile/user']);
      }
    } else {
      this.router.navigate(['/login']);
    }
  }
}
