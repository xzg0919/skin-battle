package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 *
 *评价表
 *
 * @Author 王灿
 **/
@TableName("sb_order_evaluation")
public class OrderEvaluation extends  DataEntity<Long>{
    /**
	 * 
	 */
	private static final long serialVersionUID = 2558277975710528118L;
	
	
	private Long id;
    /**
     * 订单id
     */
    private  Integer orderId;
    
    @TableField(exist=false)
    private Order order;
    /**
     * 回收人员id
     */
    private  Integer recyclerId;
    /**
     * 得分
     */
    private  Integer score;
    /**
     * 会员id
     */
    private  Integer memberId;
    
    @TableField(exist=false)
    private Member member;
    /**
     * 评论内容
     */
    private  String content;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Integer getRecyclerId() {
		return recyclerId;
	}

	public void setRecyclerId(Integer recyclerId) {
		this.recyclerId = recyclerId;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

   
    
    
}
