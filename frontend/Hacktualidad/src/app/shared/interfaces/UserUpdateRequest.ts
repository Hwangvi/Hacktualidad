export interface UserUpdateRequest {
  name: string;
  surname: string;
  address?: string;
  phone?: number | null;
  photo?: string;
}
