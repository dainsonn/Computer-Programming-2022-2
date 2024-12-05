package course;

public class Bidding implements Comparable<Bidding>{
    public int courseId;
    public int mileage;
    public Bidding(int courseId, int mileage){
        this.courseId = courseId;
        this.mileage = mileage;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Bidding){
            return courseId == ((Bidding) obj).courseId && mileage == ((Bidding) obj).mileage;
        }else{
            return false;
        }
    }

    @Override
    public int compareTo(Bidding o) {
        if(this.mileage > o.mileage){
            return -1;
            
        }else if(this.mileage < o.mileage){
            return 1;
        }else{
            if(this.courseId > o.courseId){
                return -1;
            }
            else if(this.courseId < o.courseId){
                return 1;
            }
            else{
                return 0;
            }
        }
    }
}
