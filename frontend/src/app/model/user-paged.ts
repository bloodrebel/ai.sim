import {User} from './user';

export interface UserPaged {
  
  content: User[];
  totalPages: number;
}
