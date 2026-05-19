import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink, Router } from '@angular/router';
import { ForumService } from '../../../core/service/forum.service';
import { Post } from '../../../shared/interfaces/forum';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-post-list',
  templateUrl: './postList.component.html',
  styleUrls: ['./postList.component.css'],
  imports: [CommonModule, RouterLink],
})
export class PostListComponent implements OnInit {
  posts: Post[] = [];
  topicName: string | null = null;
  isLoading = true;

  constructor(
    private route: ActivatedRoute,
    private forumService: ForumService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.topicName = this.route.snapshot.paramMap.get('topicName');

    if (this.topicName) {
      this.forumService.getPostsByTopic(this.topicName).subscribe({
        next: (data) => {
          this.posts = data;
          this.isLoading = false;
        },
        error: (err) => {
          console.error(`Error al cargar posts de ${this.topicName}:`, err);
          this.isLoading = false;
        },
      });
    }
  }

  goBack(): void {
    this.router.navigate(['/forum']);
  }
}
