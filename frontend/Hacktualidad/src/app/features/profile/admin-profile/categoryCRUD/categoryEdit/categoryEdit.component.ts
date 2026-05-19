import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';
import { CategoryService } from '../../../../../core/service/Category.service';
import { CategoryCreateUpdate } from '../../../../../core/service/Category.service';
import { Category } from '../../../../../shared/interfaces/Category';

@Component({
  selector: 'app-category-edit',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './categoryEdit.component.html',
  styleUrls: ['./categoryEdit.component.css']
})
export class CategoryEditComponent implements OnInit {

  public category: CategoryCreateUpdate = {
    name: '',
    description: ''
  };
  private categoryId: number | null = null;
  public errorMessage: string | null = null;

  constructor(
    private categoryService: CategoryService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.categoryId = +idParam;
      this.categoryService.getCategoryById(this.categoryId).subscribe({
        next: (data: Category) => {
          this.category = {
            name: data.name,
            description: data.description
          };
        },
        error: (err) => {
          this.errorMessage = 'No se pudo encontrar la categoría solicitada.';
          console.error(err);
        }
      });
    }
  }

  onSubmit(): void {
    if (!this.categoryId) {
      this.errorMessage = "Error: ID de categoría no encontrado.";
      return;
    }

    this.categoryService.updateCategory(this.categoryId, this.category).subscribe({
      next: () => {
        Swal.fire({
          title: '¡Actualizada!',
          text: 'La categoría ha sido modificada correctamente.',
          icon: 'success',
          timer: 2000,
          showConfirmButton: false,
        }).then(() => {
          this.router.navigate(['/profile/admin/categories/list']);
        });
      },
      error: (err) => {
        this.errorMessage = 'No se pudo actualizar la categoría.';
        console.error(err);
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/profile/admin/categories/list']);
  }
}
