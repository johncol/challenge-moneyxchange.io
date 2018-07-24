import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

import { CalculatorComponent } from './components/calculator/calculator.component';
import { CalculatorService } from './services/calculator/calculator.service';
import { TextMaskModule } from 'angular2-text-mask';

@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TextMaskModule
  ],
  providers: [
    CalculatorService
  ],
  declarations: [
    CalculatorComponent
  ],
  exports: [
    CalculatorComponent
  ]
})
export class ValutaCalculatorModule { }
