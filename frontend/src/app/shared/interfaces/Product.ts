import { Category } from "./Category";

export interface Product {
  productId: number;
  name: string;
  description: string;
  price: number;
  stock: number;
  active: boolean;
  photo?: string;
  category: Category;
}
