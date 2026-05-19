import { Component, OnInit } from '@angular/core';
import { ForumService } from '../../../core/service/forum.service';
import { Topic } from '../../../shared/interfaces/forum';
import { CommonModule } from '@angular/common';
import { RouterLink, ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-topic-moderation-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './topicModerationList.component.html',
  styleUrls: ['./topicModerationList.component.css']
})
export class TopicModerationListComponent implements OnInit {
  topics: Topic[] = [];
  isLoading = true;
  moderationMode: 'posts' | 'topics' = 'posts';
  title: string = 'Seleccionar Temática';
  subtitle: string = 'Elige una temática para ver y moderar sus posts.';

  constructor(
    private forumService: ForumService,
    private route: ActivatedRoute,
    private routed: Router
  ) { }

  ngOnInit(): void {
    if (this.route.snapshot.url.join('/').includes('topics/moderate')) {
      this.moderationMode = 'topics';
      this.title = 'Moderar Temáticas';
      this.subtitle = 'Elige una temática para ver sus detalles y eliminarla.';
    }

    this.forumService.getAllTopics().subscribe({
      next: (data) => {
        this.topics = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error("Error al cargar las temáticas", err);
        this.isLoading = false;
      }
    });
  }
  onCancel(): void {
    this.routed.navigate(['/profile/admin']);
  }
}
