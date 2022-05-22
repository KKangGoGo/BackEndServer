package com.kkanggogo.facealbum.album.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Sharing")
public class SharingAlbum extends Album {
}
