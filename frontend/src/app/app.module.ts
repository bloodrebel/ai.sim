import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {HttpClientModule} from '@angular/common/http';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {SimComponent} from './component/sim/sim.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {SimService} from './service/sim.service';
import {DeleteSimDialogComponent} from './component/dialog/delete-sim-dialog/delete-sim-dialog.component';
import {EditSimDialogComponent} from './component/dialog/edit-sim-dialog/edit-sim-dialog.component';
import {AuditsComponent} from './component/audits/audits.component';
import {RouterModule} from '@angular/router';
import {CreateSimDialogComponent} from './component/dialog/create-sim-dialog/create-sim-dialog.component';
import {UserComponent} from './component/user/user.component';
import {PagenotfoundComponent} from './component/pagenotfound/pagenotfound.component';
import {AboutComponent} from './component/about/about.component';
import {CreateDialogComponent} from './component/dialog/create-user-dialog/create-dialog.component';
import {EditDialogComponent} from './component/dialog/edit-user-dialog/edit-dialog.component';
import {DeleteDialogComponent} from './component/dialog/delete-user-dialog/delete-dialog.component';
import {UserService} from './service/user.service';
import {DialogModule} from './modules/dialog.module';
import {Location} from "@angular/common";
import {LoginComponent} from './component/login/login.component';
import {LoginService} from './service/login.service';
import {AuthguardGuard} from './guard/authguard.guard';
import { RegisterComponent } from './component/register/register.component';


@NgModule({
  declarations: [
    SimComponent,
    CreateSimDialogComponent,
    EditSimDialogComponent,
    DeleteSimDialogComponent,
    AppComponent,
    AuditsComponent,
    CreateSimDialogComponent,
    UserComponent,
    PagenotfoundComponent,
    AboutComponent,
    CreateDialogComponent,
    EditDialogComponent,
    DeleteDialogComponent,
    LoginComponent,
    RegisterComponent

  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    RouterModule.forRoot([
      {path: 'users', component: UserComponent, canActivate: [AuthguardGuard]},
      {path: 'about', component: AboutComponent},
      {path: 'login', component: LoginComponent},
      {path: 'register', component: RegisterComponent},
      {path: '', redirectTo: 'login', pathMatch: 'full'},
      {path: 'audits/:id', component: AuditsComponent, canActivate: [AuthguardGuard]},
      {path: 'sims', component: SimComponent, canActivate: [AuthguardGuard]},
      {path: '**', component: PagenotfoundComponent}
    ]),
    FormsModule,
    ReactiveFormsModule,
    DialogModule,
    BrowserAnimationsModule,
    BrowserModule,
    HttpClientModule
  ],
  providers: [SimComponent, SimService, UserService, LoginService, Location],
  entryComponents: [SimComponent, CreateSimDialogComponent, EditSimDialogComponent, DeleteSimDialogComponent,
    UserComponent, CreateDialogComponent, EditDialogComponent, DeleteDialogComponent],
  bootstrap: [AppComponent]
})
export class AppModule {
}
