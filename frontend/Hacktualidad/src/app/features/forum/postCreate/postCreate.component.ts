import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ForumService } from '../../../core/service/forum.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-post-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './postCreate.component.html',
  styleUrls: ['./postCreate.component.css'],
})
export class PostCreateComponent implements OnInit {
  postForm: FormGroup;
  topicName: string | null = null;
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
    this.topicName = this.route.snapshot.paramMap.get('topicName');
    if (!this.topicName) {
      this.error = 'No se ha especificado un tema para el post.';
      console.error('No se encontró topicName en la ruta');
    }
  }

  goBack(): void {
    if (this.topicName) {
      this.router.navigate(['/forum', this.topicName]);
    } else {
      this.router.navigate(['/forum']);
    }
  }

  onSubmit(): void {
    if (this.postForm.invalid || !this.topicName) {
      return;
    }

    this.isSubmitting = true;
    this.error = null;

    const postData = {
      title: this.postForm.value.title,
      content: this.postForm.value.content,
    };

    this.forumService.createPost(this.topicName, postData).subscribe({
      next: (newPost) => {
        this.isSubmitting = false;
        this.router.navigate(['/posts', newPost.postId]);
      },
      error: (err) => {
        console.error('Error al crear el post:', err);
        this.error = 'Hubo un error al crear el post. Inténtalo de nuevo.';
        this.isSubmitting = false;
      },
    });
  }
}
