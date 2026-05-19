import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Topic, Post, Comment } from '../../shared/interfaces/forum';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ForumService {
  private baseUrl = `${environment.apiUrl}/forum`;

  constructor(private http: HttpClient) {}

  getAllTopics(): Observable<Topic[]> {
    return this.http.get<Topic[]>(`${this.baseUrl}/topics`);
  }

  getPostsByTopic(topicName: string): Observable<Post[]> {
    return this.http.get<Post[]>(`${this.baseUrl}/topics/${topicName}/posts`);
  }

  getPostById(postId: number): Observable<Post> {
    return this.http.get<Post>(`${this.baseUrl}/posts/${postId}`);
  }

  createPost(topicName: string, postData: { title: string; content: string }): Observable<Post> {
    return this.http.post<Post>(`${this.baseUrl}/topics/${topicName}/posts`, postData);
  }

  deletePost(postId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/posts/${postId}`);
  }
  addCommentToPost(postId: number, commentData: { content: string }): Observable<Comment> {
    return this.http.post<Comment>(`${this.baseUrl}/posts/${postId}/comments`, commentData);
  }
  createTopicWithPhoto(formData: FormData): Observable<Topic> {
    return this.http.post<Topic>(`${this.baseUrl}/topics`, formData, { withCredentials: true });
  }
  updatePost(postId: number, postData: { title: string; content: string }): Observable<Post> {
    return this.http.put<Post>(`${this.baseUrl}/posts/${postId}`, postData);
  }
  deleteTopic(topicId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/topics/${topicId}`, { withCredentials: true });
  }

  getAllPosts(): Observable<Post[]> {
    return this.http.get<Post[]>(`${this.baseUrl}/posts`);
  }

  deleteComment(commentId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/comments/${commentId}`, { withCredentials: true });
  }
}
