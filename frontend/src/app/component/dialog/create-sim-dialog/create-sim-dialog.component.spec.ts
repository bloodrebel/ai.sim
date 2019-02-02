import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateSimDialogComponent } from './create-sim-dialog.component';

describe('CreateSimDialogComponent', () => {
  let component: CreateSimDialogComponent;
  let fixture: ComponentFixture<CreateSimDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CreateSimDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateSimDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
