import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ForumService } from '../../../core/service/forum.service';
import { Post } from '../../../shared/interfaces/forum';
import { CommonModule } from '@angular/common';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-post-edit',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './postEdit.component.html',
  styleUrls: ['./postEdit.component.css'],
})
export class PostEditComponent implements OnInit {
  postForm: FormGroup;
  post: Post | null = null;
  postId: number | null = null;
  isLoading = true;
  isSubmitting = false;
  error: string | null = null;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private forumService: ForumService
  ) {
    this.postForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(5)]],
      content: ['', [Validators.required, Validators.minLength(20)]],
    });
  }

  ngOnInit(): void {
    const idFromRoute = this.route.snapshot.paramMap.get('postId');
    if (idFromRoute) {
      this.postId = Number(idFromRoute);
      this.loadPostData();
    } else {
      this.isLoading = false;
      this.error = 'ERROR: No se encontró un ID de post en la URL.';
    }
  }

  loadPostData(): void {
    if (!this.postId) return;

    this.forumService.getPostById(this.postId).subscribe({
      next: (data) => {
        this.post = data;
        this.postForm.patchValue({
          title: data.title,
          content: data.content,
        });
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = false;
        this.error = 'No se pudo cargar el post para editarlo.';
        console.error(err);
      },
    });
  }

  onSubmit(): void {
    if (this.postForm.invalid || !this.postId) {
      return;
    }

    this.isSubmitting = true;
    const updatedData = this.postForm.value;

    this.forumService.updatePost(this.postId, updatedData).subscribe({
      next: (updatedPost) => {
        this.isSubmitting = false;
        Swal.fire({
          icon: 'success',
          title: '¡Post Actualizado!',
          text: `El post "${updatedPost.title}" se ha modificado correctamente.`,
          timer: 2000,
          timerProgressBar: true,
          background: '#0a110a',
          color: '#9affb4',
        }).then(() => {
          this.router.navigate(['/posts', this.postId]);
        });
      },
      error: (err) => {
        this.isSubmitting = false;
        Swal.fire({
          icon: 'error',
          title: 'Error de Modificación',
          text: 'No se pudo actualizar el post.',
          background: '#0a110a',
          color: '#9affb4',
          confirmButtonColor: '#ff3333',
        });
        console.error(err);
      },
    });
  }

  onCancel(): void {
    if (this.postId) {
      this.router.navigate(['/posts', this.postId]);
    } else {
      this.router.navigate(['/profile/admin/forum/moderate']);
    }
  }
}
