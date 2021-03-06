package com.project.inspection;

import com.project.interfacebuilder.Selector;

public class InformationPropertyInfo extends PropertyInfo {

	private String displayName;
	private String desc;
	private OrderType orderType;
	private FiniteType finiteType;
	private MultipleType multipleType;
	private int cardinality;
	private AlignType alignType;
	private Selector selector=null;
	private int width;

	public InformationPropertyInfo(EntityInfo eInfo, String propertyName,
			String displayName, String desc, Class<?> type, int width,
			OrderType orderType, FiniteType finiteType,
			MultipleType multipleType, int cardinality, AlignType alignType,
			String readMethod, String writeMethod) {
		super(eInfo, propertyName, type, 
				readMethod, writeMethod);
		this.displayName = displayName;
		this.orderType = orderType; 
		this.finiteType = finiteType; 
		this.multipleType = multipleType;
		this.cardinality = cardinality;
		this.alignType = alignType;
		this.setDesc(desc); 
		this.width = width; 
	}

	public String getDisplayName() {
		return displayName;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public FiniteType getFiniteType() {
		return finiteType;
	}

	public MultipleType getMultipleType() {
		return multipleType;
	}

	public int getCardinality() {
		return cardinality;
	}

	public AlignType getAlignType() {
		return alignType;
	}

	public Selector getSelector() {
		return selector;
	}

	public void setSelector(Selector selector) {
		this.selector = selector;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	@Override
	public boolean isInformation(){
		return true;
	}

}
