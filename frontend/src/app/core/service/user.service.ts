import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../../shared/interfaces/User';
import { AdminUserCreateRequest } from '../../shared/interfaces/userRegisterInAdmin';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiUrl = `${environment.apiUrl}/users`;

  constructor(private http: HttpClient) {}

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl, { withCredentials: true });
  }

  getUserById(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${id}`, { withCredentials: true });
  }

  createUser(userData: AdminUserCreateRequest): Observable<User> {
    return this.http.post<User>(this.apiUrl, userData, { withCredentials: true });
  }

  createUserWithPhoto(formData: FormData): Observable<User> {
    const createUrl = `${this.apiUrl}/create`;
    return this.http.post<User>(createUrl, formData, { withCredentials: true });
  }

  updateUser(id: number, formData: FormData): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/${id}`, formData, { withCredentials: true });
  }

  deleteUser(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, { withCredentials: true });
  }

  register(formData: FormData): Observable<User> {
    const registerUrl = `${this.apiUrl}/register`;
    return this.http.post<User>(registerUrl, formData);
  }

  login(credentials: any): Observable<any> {
    const loginUrl = `${this.apiUrl}/login`;
    return this.http.post(loginUrl, credentials);
  }
}
