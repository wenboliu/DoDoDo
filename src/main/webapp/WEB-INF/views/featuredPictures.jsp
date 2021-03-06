<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<section class="featured-picture">
    <h4>Featured Pictures</h4>

    <p>${error}</p>
    <ul>
        <c:forEach var="picList" items="${featuredPicList}">
            <li class="picInfo">
                <a class="thickbox" href="#TB_inline?height=300&amp;width=600&amp;inlineId=pictureContent">
                    <img class="smallImage" src="http://7u2rkn.com1.z0.glb.clouddn.com/${picList.id}" alt="${picList.id}" />
                </a>
                <p>Caption: <c:out value="${picList.caption}"/></p>
                <p>$<c:out value="${picList.price}"/></p>
            </li>
        </c:forEach>
    </ul>
</section>

<div id="pictureContent" style="display: none">
    <img class="biggerImage" src="" />

    <div class="pictureInformation">
        Caption:<p class="pictureCaption"></p>
        Keyword:<p class="pictureKeyword"></p>
        Location:<p class="pictureLocation"></p>
        Price:<p class="picturePrice"></p>
        <button type="button">Add Cart</button>
    </div>

</div>

