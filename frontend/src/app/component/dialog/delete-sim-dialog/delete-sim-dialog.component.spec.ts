import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteSimDialogComponent } from './delete-sim-dialog.component';

describe('DeleteSimDialogComponent', () => {
  let component: DeleteSimDialogComponent;
  let fixture: ComponentFixture<DeleteSimDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DeleteSimDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DeleteSimDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
