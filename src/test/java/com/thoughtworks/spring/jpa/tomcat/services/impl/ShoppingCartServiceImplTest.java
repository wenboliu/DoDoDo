package com.thoughtworks.spring.jpa.tomcat.services.impl;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.thoughtworks.spring.jpa.tomcat.commons.ShoppingCartStatus;
import com.thoughtworks.spring.jpa.tomcat.commons.json.ShoppingCartResponse;
import com.thoughtworks.spring.jpa.tomcat.dao.PictureDao;
import com.thoughtworks.spring.jpa.tomcat.dao.ShoppingCartDao;
import com.thoughtworks.spring.jpa.tomcat.entities.Picture;
import com.thoughtworks.spring.jpa.tomcat.entities.ShoppingCart;
import com.thoughtworks.spring.jpa.tomcat.services.ShoppingCartService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ShoppingCartServiceImplTest {

    @InjectMocks
    ShoppingCartService shoppingCartService = new ShoppingCartServiceImpl();
    @Mock
    ShoppingCartDao shoppingCartDao;
    @Mock
    PictureDao pictureDao;
    String userId;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        userId = "1";
    }

    @Test
    public void shouldGetPicListByUserId() {
        List<ShoppingCart> shoppingCartList = new ArrayList<>();
        ShoppingCart sc = new ShoppingCart();
        shoppingCartList.add(sc);
        when(shoppingCartDao.getShoppingCarByUserId(anyString())).thenReturn(Optional.of(shoppingCartList));
        Picture picture = new Picture();
        picture.setId("123456");
        when(pictureDao.getPicById(anyString())).thenReturn(Optional.of(picture));
        ImmutableList<Picture> picListByUserId = shoppingCartService.getPicListByUserId(userId);
        assertThat(picListByUserId.get(0).getId(), is(picture.getId()));
    }

    @Test
    public void shouldReturnEmptyListWhenShoppingCarIsEmpty() {
        when(shoppingCartDao.getShoppingCarByUserId(anyString())).thenReturn(Optional.<List<ShoppingCart>>absent());
        ImmutableList<Picture> picListByUserId = shoppingCartService.getPicListByUserId(userId);
        assertThat(picListByUserId.size(), is(0));
    }

    @Test
    public void shouldReturnFilteredPictureListWhenSomeItemsInShoppingCartIsExpired() {
        List<ShoppingCart> shoppingCartList = getShoppingCartList();
        when(shoppingCartDao.getShoppingCarByUserId(anyString())).thenReturn(Optional.of(shoppingCartList));
        when(pictureDao.getPicById(anyString())).thenReturn(Optional.<Picture>absent());
        ImmutableList<Picture> picListByUserId = shoppingCartService.getPicListByUserId(userId);
        assertThat(picListByUserId.size(), is(0));
    }

    private List<ShoppingCart> getShoppingCartList() {
        List<ShoppingCart> shoppingCartList = new ArrayList<>();
        ShoppingCart sc1 = new ShoppingCart();
        sc1.setPicId("123456");
        sc1.setId((long) 123456);
        sc1.setUserId(Long.parseLong(userId));
        shoppingCartList.add(sc1);
        return shoppingCartList;
    }

    @Test
    public void shouldReturnSuccessWhenAddShoppingCartSuccess() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId((long) 123);
        String picId = "123456";
        shoppingCart.setPicId(picId);
        shoppingCart.setId((long) 123456);

        Picture picture = new Picture();
        picture.setId(picId);

        when(pictureDao.getPicById(anyString())).thenReturn(Optional.of(picture));

        ShoppingCartResponse shoppingCartResponse = shoppingCartService.addShoppingCar(shoppingCart);

        Mockito.verify(shoppingCartDao).persist(shoppingCart);
        assertThat(shoppingCartResponse.getStatus(), is(ShoppingCartStatus.SUCCESS));
    }

    @Test
    public void shouldReturnPictureNotExistWhenAddShoppingCartPictureNotExist() {
        ShoppingCart shoppingCart = new ShoppingCart();

        when(pictureDao.getPicById(anyString())).thenReturn(Optional.<Picture>absent());

        ShoppingCartResponse shoppingCartResponse = shoppingCartService.addShoppingCar(shoppingCart);

        assertThat(shoppingCartResponse.getStatus(), is(ShoppingCartStatus.PICTURE_NOT_EXIT));
    }
}