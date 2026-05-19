export interface UserRegister {
  name: string;
  surname: string;
  address: string;
  phone: number | null;
  photo: string;
  email: string;
  password: string;
  confirmPassword: string;
}
