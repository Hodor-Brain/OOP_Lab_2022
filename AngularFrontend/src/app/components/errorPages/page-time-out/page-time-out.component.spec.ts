import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageTimeOutComponent } from './page-time-out.component';

describe('PageTimeOutComponent', () => {
  let component: PageTimeOutComponent;
  let fixture: ComponentFixture<PageTimeOutComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PageTimeOutComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PageTimeOutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
