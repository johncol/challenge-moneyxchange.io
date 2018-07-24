import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';

import { Valuta, AVAILABLE_VALUTAS } from '../../../shared/models';
import { CalculatorService } from '../../services/calculator';
import { NotificationService } from '../../../shared/services/notification';
import { LoaderService } from '../../../shared/services/loader';
import { MaskBuilderService } from '../../../shared/services/mask-builder';

@Component({
  selector: 'mxc-calculator',
  templateUrl: './calculator.component.html',
  styleUrls: ['./calculator.component.scss']
})
export class CalculatorComponent implements OnInit, OnDestroy {
  private subscriptions: Subscription[] = [];

  form: FormGroup;

  source: Valuta = AVAILABLE_VALUTAS.USD;
  target: Valuta = AVAILABLE_VALUTAS.EUR;
  sourceMask = this.maskBuilder.createMask({ prefix: this.source.symbol });
  targetMask = this.maskBuilder.createMask({ prefix: this.target.symbol });

  constructor(
    public service: CalculatorService,
    private maskBuilder: MaskBuilderService,
    private formBuilder: FormBuilder,
    private notificationService: NotificationService,
    private loader: LoaderService
  ) { }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      source: [null, [Validators.required, Validators.min(1)]],
      target: [{ value: null, disabled: true }]
    });
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(subscription => subscription.unsubscribe());
  }

  updateTargetValue(): void {
    this.loader.displayLoader();
    const subscription: Subscription = this.service
      .calculateValue(this.source, this.target, this.getSourceValue())
      .subscribe({
        next: this.handleSuccessResponse.bind(this),
        error: this.handleError.bind(this)
      });
    this.subscriptions.push(subscription);
  }

  private getSourceValue(): number {
    return Number(this.form.get('source').value);
  }

  private handleSuccessResponse(targetValue: number): void {
    this.updateTargetFieldValue(targetValue);
    this.loader.hideLoader();
  }

  private handleError(error: any): void {
    console.error(error);
    this.showErrorNotification();
    this.loader.hideLoader();
  }

  private updateTargetFieldValue(targetValue: number): void {
    this.form.get('target').setValue(targetValue);
  }

  private showErrorNotification(): void {
    this.notificationService.displayNotification({
      title: 'Try again later',
      message: 'Error while getting the rates'
    });
  }
}
