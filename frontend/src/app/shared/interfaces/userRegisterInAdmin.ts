export interface AdminUserCreateRequest {
  name: string;
  surname: string;
  address: string;
  phone: number | null;
  photo: string,
  email: string;
  password: string;
  confirmPassword: string;
  role: 'ADMIN' | 'USER';
}
