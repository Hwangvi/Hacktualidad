export interface User {
  userId: number;
  name: string;
  surname: string;
  email: string;
  address?: string;
  phone?: number | null;
  photo?: string;
  role: 'ADMIN' | 'USER';
}
