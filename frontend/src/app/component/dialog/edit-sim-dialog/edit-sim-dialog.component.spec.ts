import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditSimDialogComponent } from './edit-sim-dialog.component';

describe('EditSimDialogComponent', () => {
  let component: EditSimDialogComponent;
  let fixture: ComponentFixture<EditSimDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditSimDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditSimDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
