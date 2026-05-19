import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import Swal from 'sweetalert2';
import { ForumService } from '../../../../../core/service/forum.service';

export interface TopicCreateRequest {
  topicName: string;
  topicDescription: string;
}

@Component({
  selector: 'app-topic-create',
  standalone: true,
  imports: [ CommonModule, FormsModule, RouterModule ],
  templateUrl: './topicCreate.component.html',
  styleUrls: ['./topicCreate.component.css']
})
export class TopicCreateComponent {

  public topic: TopicCreateRequest = {
    topicName: '',
    topicDescription: ''
  };

  public selectedFile: File | null = null;
  public imagePreview: string | ArrayBuffer | null = null;

  constructor(
    private forumService: ForumService,
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
    if (!this.selectedFile) {
        Swal.fire('Error', 'Debes seleccionar una imagen de fondo.', 'error');
        return;
    }

    const formData = new FormData();
    formData.append('topic', new Blob([JSON.stringify(this.topic)], { type: 'application/json' }));
    formData.append('file', this.selectedFile, this.selectedFile.name);

    this.forumService.createTopicWithPhoto(formData).subscribe({
      next: () => {
        Swal.fire({
          title: '¡Temática Creada!',
          text: 'La nueva temática ha sido añadida con éxito.',
          icon: 'success'
        }).then(() => this.router.navigate(['/forum']));
      },
      error: (err) => {
        Swal.fire('Error', err.error.message || 'No se pudo crear la temática.', 'error');
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/profile/admin']);
  }
}
