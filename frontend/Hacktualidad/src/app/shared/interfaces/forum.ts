export interface Author {
  userId: number;
  name: string;
  photo: string;
}

export interface Post {
  postId: number;
  title: string;
  content: string;
  publicationDate: string;
  author: Author;
  comments: Comment[];
  topicName: string;
}

export interface Comment {
    commentId: number;
    content: string;
    commentDate: string;
    author: Author;
}

export interface Topic {
    topicId: number;
    topicName: string;
    topicDescription: string;
    backgroundImage: string;
}
