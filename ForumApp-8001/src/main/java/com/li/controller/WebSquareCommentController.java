package com.li.controller;

import com.li.entity.pojo.SquareComment;
import com.li.entity.pojo.User;
import com.li.entity.vo.SquareCommentVO;
import com.li.mapper.SquareCommentMapper;
import com.li.service.SquareCommentService;
import com.li.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@io.swagger.annotations.Api(tags = "Web圈子评论接口")
@RestController
@RequestMapping("/webSquareComment")
public class WebSquareCommentController {

  @Autowired private SquareCommentMapper squareCommentMapper;

  @Autowired private SquareCommentService squareCommentService;

  @Autowired private UserService userService;

  @ApiOperation(value = "增加评论")
  @PostMapping("/addPlanetComment")
  @ApiImplicitParam(name = "PlanetComment", value = "评论分类对象", required = true)
  public boolean addPlanetComment(@RequestBody SquareComment SquareComment) {
    return squareCommentService.save(SquareComment);
  }

  @ApiOperation(value = "根据文章id查看对应评论数")
  @ApiImplicitParam(name = "planetId", value = "评论id", required = true)
  @GetMapping("/getPlanetCommentNum/{planetId}")
  public int getPlanetCommentNum(@PathVariable("planetId") Integer planetId) {
    return squareCommentService.GetCommentNum(planetId);
  }

  //    @ApiOperation(value = "查询评论(全部)")
  //    @GetMapping("/getallPlanetComment")
  //    public List<SquareComment> getallPlanetComment(
  //    ) {
  //        //条件查询 parentId为0的为根评论
  //        QueryWrapper<SquareComment> wrapper = new QueryWrapper<>();
  //        wrapper.eq("parent_id",0);
  //
  //        return  squareCommentMapper.selectList(wrapper);
  //    }

  @ApiOperation(value = "评论点赞")
  @ApiImplicitParam(name = "id", value = "评论id", required = true)
  @GetMapping("/likeClickComment/{id}")
  public Boolean likeClickComment(@PathVariable("id") Integer id) {
    return squareCommentMapper.resourceLoveBrowse(id);
  }

  @ApiOperation(value = "根据文章id查询评论")
  @ApiImplicitParam(name = "postId", value = "postId", required = true)
  @GetMapping("/getPlanetIdComment/{postId}")
  public List<SquareCommentVO> getPlanetIdComment(@PathVariable("postId") Integer postId) {
    List<SquareCommentVO> result = new ArrayList<>();
    // 获取不是回复的评论
    List<SquareComment> squareComments=squareCommentService.getCommentByIdOrder(postId,0,"add_time");

    // 分解planetComments，并把PlanetCommentVO中的reply赋值为planetComment
    for (SquareComment commentOrigin : squareComments) {
      SquareCommentVO planetCommentVO = new SquareCommentVO();

      // 获取发布人的信息
      User userOrigin  = userService.getById(commentOrigin.getUserId());
      if(userOrigin==null){ // 脏数据放弃取回
        continue;
      }
      commentOrigin.setReviewers(userOrigin.getNickName());
      commentOrigin.setResponderId(commentOrigin.getUserId());
      commentOrigin.setProfile(userOrigin.getAvatar());

      List<SquareComment> squareCommentsChildren = squareCommentService.
              getCommentByIdOrder(-1,commentOrigin.getId(),"add_time");
      for (SquareComment comment : squareCommentsChildren) {
        // 获取每条评论的发布人的信息和回复人的信息，因为评论的回复可能是自己回复自己，所以不能直接用commentOrigin的信息
        User userFrom = userService.getById(comment.getUserId());
        if (userFrom==null){
          continue;
        }
        if (comment.getToUserId() != 0) {
          User userTo = userService.getById(comment.getToUserId());
          if (userTo==null){
            continue;
          }
          comment.setResponder(userTo.getNickName());
          comment.setResponderId(userTo.getUserId());
        }
        comment.setReviewers(userFrom.getNickName());
        comment.setReviewersId(userFrom.getUserId());
        comment.setProfile(userFrom.getAvatar());
      }
      planetCommentVO.setReply(squareCommentsChildren);
      BeanUtils.copyProperties(commentOrigin, planetCommentVO);

      result.add(planetCommentVO);
    }
    return result;
  }

  //
  //    @ApiOperation(value = "最新评论")
  //    @ApiImplicitParam(name = "num",value = "数量",required = true)
  //    @GetMapping("/getNewArticleComment/{num}")
  //    public List<ArticleCommentVO> getNewArticleComment(
  //            @PathVariable("num") Integer num
  //    ) {
  //        List<ArticleCommentVO> result = new ArrayList<>();
  //        QueryWrapper<ArticleComment> wrapper = new QueryWrapper<>();
  //        wrapper.orderByDesc("add_time");
  //        //使用last方法拼接sql语句
  //        wrapper.last("limit "+ num);
  //        List<ArticleComment> ArticleComments =  PlanetCommentMapper.selectList(wrapper);
  //        for (ArticleComment ArticleComment : ArticleComments) {
  //            ArticleCommentVO ArticleCommentVO = new ArticleCommentVO();
  //            Integer articleId = ArticleComment.getArticleId();
  //            //根据文章id查询文章名称
  //            Article article = articleMapper.selectById(articleId);
  //            String title = article.getTitle();
  //            ArticleCommentVO.setArticleName(title);
  //            BeanUtils.copyProperties(ArticleComment,ArticleCommentVO);
  //
  //            result.add(ArticleCommentVO);
  //        }
  //        return result;
  //    }

}
