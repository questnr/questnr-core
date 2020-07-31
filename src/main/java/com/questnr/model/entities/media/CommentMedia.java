package com.questnr.model.entities.media;

import org.hibernate.search.annotations.Indexed;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Indexed
@Table(name = "qr_comment_media")
public class CommentMedia extends Media {

}
