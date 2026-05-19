import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';
import { CategoryService } from '../../../../../core/service/Category.service';
import { CategoryCreateUpdate } from '../../../../../core/service/Category.service';

@Component({
  selector: 'app-category-create',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './categoryCreate.component.html',
  styleUrls: ['./categoryCreate.component.css']
})
export class CategoryCreateComponent {

  public category: CategoryCreateUpdate = {
    name: '',
    description: ''
  };

  constructor(
    private categoryService: CategoryService,
    private router: Router
  ) {}

  onSubmit(): void {
    this.categoryService.createCategory(this.category).subscribe({
      next: () => {
        Swal.fire({
          title: '¡Categoría Creada!',
          text: 'La nueva categoría ha sido añadida con éxito.',
          icon: 'success',
          timer: 2000,
          showConfirmButton: false
        }).then(() => this.router.navigate(['/profile/admin/categories/list']));
      },
      error: (err) => {
        console.error('Error al crear la categoría:', err);
        Swal.fire('Error', 'No se pudo crear la categoría.', 'error');
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/profile/admin']);
  }
}
