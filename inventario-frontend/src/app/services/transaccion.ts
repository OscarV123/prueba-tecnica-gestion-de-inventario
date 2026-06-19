export interface Transaccion {
  id?: number;
  productoId: number;
  tipo: string;
  cantidad: number;
  motivo: string;
  fechaRegistro?: string;
}
