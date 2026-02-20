export interface Product {
  id: string;
  name: string;
  unitPrice: number;
  stock: number;
  sku: string;
}

export interface PagedProductsResponse {
  items: Product[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}
