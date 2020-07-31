package com.questnr.model.entities.media;

import org.hibernate.search.annotations.Indexed;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Indexed
@Table(name = "qr_post_media")
public class PostMedia extends Media{

}
