package net.whend.soodal.whend.util.quitview;

public class QuiltViewPatch implements Comparable{
	public int width_ratio;
	public int height_ratio;
	
	public QuiltViewPatch(int width_ratio, int height_ratio){
		this.width_ratio = width_ratio;
		this.height_ratio = height_ratio;
	}
	
	private static QuiltViewPatch create(Size size){
		switch(size){
		case Big:
			return new QuiltViewPatch(3,1);

		case Small:
			return new QuiltViewPatch(1,1);

		case Tall:
			return new QuiltViewPatch(1,2);

		case Square:
			return new QuiltViewPatch(2,2);

		}
		
		return new QuiltViewPatch(1,1);
	}
	
	public int getHeightRatio(){return this.height_ratio;}
	public int getWidthRatio(){return this.width_ratio;}
	
	public static QuiltViewPatch create(int view_count){
		
		if(view_count == 0)
			return new QuiltViewPatch(2,2);
		else if((view_count % 11) == 0)
			return new QuiltViewPatch(2,2);
		else if((view_count % 4) == 0)
			return new QuiltViewPatch(1,2);
		else 
			return new QuiltViewPatch(1,1);
		
	}
	
	private enum Size{
		Big,
		Small,
		Tall,
		Square
	}
	
	public static QuiltViewPatch init2(int position, int column){

		return create(Size.Big);
	}

	public static QuiltViewPatch init(int position, int column){

		return init4(position);
	}

	private static QuiltViewPatch init4(int position){
		switch(position % 29){
			case 0:
				return create(Size.Big);
			case 1:
				return create(Size.Small);
			case 2:
				return create(Size.Square);
			case 3:
				return create(Size.Small);
			case 4:
				return create(Size.Tall);
			case 5:
			case 6:
				return create(Size.Small);
			case 7:
				return create(Size.Square);
			case 8:
			case 9:
			case 10:
			case 11:
				return create(Size.Small);
			case 12:
				return create(Size.Big);
			case 13:
				return create(Size.Square);
			case 14:
			case 15:
				return create(Size.Small);
			case 16:
				return create(Size.Tall);
			case 17:
				return create(Size.Square);
			case 18:
			case 19:
				return create(Size.Small);
			case 20:
				return create(Size.Tall);
			case 21:
				return create(Size.Tall);
			case 22:
			case 23:
			case 24:
				return create(Size.Small);
			case 25:
				return create(Size.Big);
			case 26:
			case 27:
			case 28:
				return create(Size.Small);


		}

		return create(Size.Small);
	}

	private static QuiltViewPatch initsample(int position){
		switch(position % 35){
		case 0:
			return create(Size.Big);
		case 1:
		case 2:
		case 3:
			return create(Size.Small);
		case 4:
			return create(Size.Tall);
		case 5:
		case 6:
		case 7:
			return create(Size.Small);
		case 8:
			return create(Size.Tall);
		case 9:
		case 10:
		case 11:
			return create(Size.Small);
		case 12:
			return create(Size.Small);
		case 13:
			return create(Size.Tall);
		case 14:
		case 15:
		case 16:
			return create(Size.Small);
		case 17:
			return create(Size.Tall);
		case 18:
		case 19:
		case 20:
			return create(Size.Small);
		case 21:
			return create(Size.Tall);
		case 22:
		case 23:
		case 24:
			return create(Size.Small);
		case 25:
			return create(Size.Tall);
		case 26:
			return create(Size.Small);
		case 27:
			return create(Size.Tall);
		case 28:
		case 29:
		case 30:
			return create(Size.Small);
		case 31:
			return create(Size.Tall);
		case 32:
			return create(Size.Small);
		case 33:
			return create(Size.Tall);
		case 34:
			return create(Size.Small);
			
		}
		
		return create(Size.Small);
	}
	
	public static boolean getRandomBoolean(){
		return (Math.random() < 0.5);
	}
	
	public boolean equals(Object obj){
		if(obj != null && obj instanceof QuiltViewPatch){
			QuiltViewPatch size = (QuiltViewPatch)obj;
			return size.height_ratio == this.height_ratio && size.width_ratio == this.width_ratio;
		}
		
		return false;
	}
	
	public int hashCode(){
		return height_ratio + 100 * width_ratio;
	}
	
	public String toString(){
		return "Patch: " + height_ratio +  " x " + width_ratio;
	}

	@Override
	public int compareTo(Object another) {
		if(another != null && another instanceof QuiltViewPatch){
			QuiltViewPatch size = (QuiltViewPatch)another;
			if(size.equals(this))
				return 0;
			
			if(this.height_ratio < size.height_ratio)
				return -1;
			else if(this.height_ratio > size.height_ratio)
				return 1;
			
			if(this.width_ratio < size.width_ratio)
				return -1;
			else
				return 1;
		}
		return -1;
	}
}
