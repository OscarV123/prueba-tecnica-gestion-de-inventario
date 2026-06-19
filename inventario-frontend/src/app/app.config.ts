import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http'; // <-- ESTA ES LA CLAVE
import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(), // <-- Esto permite usar el servicio HTTP en todo tu proyecto
  ],
};
