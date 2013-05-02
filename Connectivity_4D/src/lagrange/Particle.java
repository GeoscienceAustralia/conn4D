package lagrange;

import java.text.NumberFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Generic particle class.
 */

public class Particle {

	private long id;
	private double x,y,z;
	private double px, py, pz;
	private double x0, y0, z0;
	private double u = 0d, v = 0d, w = 0d;
	private long t;
	private long birthday = 0;
	private long competencyStart = 0;
	private int tm, k, i, j;
	private boolean settling = false;
	private boolean dead = false;
	private boolean lost = false;
	private boolean error = false;
	private boolean finished = false;
	private boolean recording = true;
	private boolean nodata = false;
	private boolean nearNoData = false;
	private boolean hadNoData = false;
	private double distance = 0.0d;
	private String source;
	private String destination;
	private Set<Long> visited = new HashSet<Long>();
	private String comments;

	/**
	 * Indicates if the Particle is eligible for settling
	 * 
	 * @return
	 */

	public boolean canSettle() {
		return settling;
	}

	/**
	 * Retrieve the age of the particle (milliseconds)
	 * 
	 * @return
	 */

	public long getAge() {
		return this.t - this.birthday;
	}

	/**
	 * Retrieves the birth timestamp of the particle (milliseconds)
	 * 
	 * @return
	 */

	public long getBirthday() {
		return birthday;
	}

	/**
	 * Retrieves comment string (for storing general information)
	 * 
	 * @return
	 */

	public String getComments() {
		return comments;
	}

	/**
	 * Retrieves the duration after which the Particle becomes competent to
	 * settle
	 * 
	 * @return
	 */

	public long getCompetencyStart() {
		return competencyStart;
	}

	/**
	 * Retrieve the current coordinates of the particle
	 * 
	 * @return
	 */

	public double[] getCoords() {
		return new double[] { x, y, z };
	}

	/**
	 * Retrieve the settlement destination of the Particle
	 * 
	 * @return
	 */

	public String getDestination() {
		return destination;
	}

	/**
	 * Retrieve the distance traveled by the Particle (meters)
	 * 
	 * @return
	 */

	public double getDistance() {
		return distance;
	}

	/**
	 * Retrieve the unique identifier of the particle
	 * 
	 * @return
	 */

	public long getID() {
		return id;
	}

	/**
	 * Identify whether the particle encountered a NoData value in the velocity
	 * field.
	 * 
	 * @return
	 */

	public boolean getNodata() {
		return nodata;
	}
	
	/**
	 * Retrieve the original X Coordinate of the particle
	 * 
	 * @return
	 */

	public double getX0() {
		return x0;
	}

	/**
	 * Retrieve the Previous Y Coordinate of the particle
	 * 
	 * @return
	 */

	public double getY0() {
		return y0;
	}

	/**
	 * Retrieve the Previous Z Coordinate of the particle
	 * 
	 * @return
	 */

	public double getZ0() {
		return z0;
	}

	/**
	 * Retrieve the Previous X Coordinate of the particle
	 * 
	 * @return
	 */

	public double getPX() {
		return px;
	}

	/**
	 * Retrieve the Previous Y Coordinate of the particle
	 * 
	 * @return
	 */

	public double getPY() {
		return py;
	}

	/**
	 * Retrieve the Previous Z Coordinate of the particle
	 * 
	 * @return
	 */

	public double getPZ() {
		return pz;
	}

	/**
	 * Retrieves the release source ID for the Particle
	 * 
	 * @return
	 */

	public String getSource() {
		return source;
	}

	/**
	 * Retrieve the Time Coordinate of the particle
	 * 
	 * @return
	 */

	public long getT() {
		return t;
	}

	/**
	 * Retrieve the last E-W velocity of the particle
	 * 
	 * @return
	 */

	public double getU() {
		return u;
	}

	/**
	 * Retrieve the last N-S velocity of the particle
	 * 
	 * @return
	 */

	public double getV() {
		return v;
	}

	/**
	 * Retrieve the set of visited destinations
	 * 
	 * @return
	 */

	public Set<Long> getVisited() {
		return visited;
	}

	/**
	 * Retrieve the last vertical velocity of the particle
	 * 
	 * @return
	 */

	public double getW() {
		return w;
	}

	/**
	 * Retrieve the X coordinate of the particle
	 * 
	 * @return
	 */

	public double getX() {
		return x;
	}

	/**
	 * Retrieve the Y coordinate of the particle
	 * 
	 * @return
	 */

	public double getY() {
		return y;
	}

	/**
	 * Retrieve the Z Coordinate of the particle
	 * 
	 * @return
	 */

	public double getZ() {
		return z;
	}

	/**
	 * Indicates if the Particle is dead
	 * 
	 * @return
	 */

	public boolean isDead() {
		return dead;
	}

	/**
	 * Indicates if the Particle has completed its run
	 * 
	 * @return
	 */

	public boolean isFinished() {
		return finished;
	}

	/**
	 * Indicates if the particle was lost - e.g. through exiting boundaries
	 * 
	 * @return
	 */

	public boolean isLost() {
		return lost;
	}

	/**
	 * Indicates if the Particle encountered a NoData value in the velocity
	 * field
	 * 
	 * @return
	 */

	public boolean isNearNoData() {
		return nearNoData;
	}

	/**
	 * Indicates whether the particle date should be recorded (by the Writers)
	 * 
	 * @return
	 */

	public boolean recording() {
		return recording;
	}

	/**
	 * Sets the birthday (millisecond time) of the Particle
	 * 
	 * @param birthday
	 */

	public void setBirthday(long birthday) {
		this.birthday = birthday;
	}

	/**
	 * Sets the value of a general information String attached to the Particle
	 * 
	 * @param competencyStart
	 */

	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * Sets the duration after which the Particle is eligible to settle
	 * (milliseconds)
	 * 
	 * @param competencyStart
	 */

	public void setCompetencyStart(long competencyStart) {
		this.competencyStart = competencyStart;
	}

	/**
	 * Sets the Particle to be dead
	 * 
	 * @param dead
	 */

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	/**
	 * Sets the settlement destination of the particle.
	 * 
	 * @param destination
	 */

	public void setDestination(String destination) {
		this.destination = destination;
	}

	/**
	 * Sets the distance traveled by the Particle (meters)
	 * 
	 * @param distance
	 */

	public void setDistance(double distance) {
		this.distance = distance;
	}

	/**
	 * Sets whether the particle encountered an error during processing
	 * 
	 * @param error
	 */

	public void setError(boolean error) {
		this.error = error;
	}

	/**
	 * Sets
	 * 
	 * @param finished
	 */

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	/**
	 * Sets the unique ID of the particle
	 * 
	 * @param id
	 */

	public void setID(long id) {
		this.id = id;
	}

	/**
	 * Sets whether the particle was lost (e.g. due to leaving bounds)
	 * 
	 * @param lost
	 */

	public void setLost(boolean lost) {
		this.lost = lost;
	}

	/**
	 * Sets whether the particle is near a NoData element in the velocity field
	 * 
	 * @param nearNoData
	 */

	public void setNearNoData(boolean nearNoData) {
		this.nearNoData = nearNoData;
	}

	/**
	 * Sets whether the particle is on top of a NoData element in the velocity
	 * field
	 * 
	 * @param nodata
	 */

	public void setNodata(boolean nodata) {
		this.nodata = nodata;
		if (nodata) {
			this.hadNoData = true;
		}
	}

	/**
	 * Sets the previous X (East-West) value of the Particle
	 * 
	 * @param px
	 */

	public void setPX(double px) {
		this.px = px;
	}

	/**
	 * Sets the previous Y (North-South) value of the Particle
	 * 
	 * @param py
	 */

	public void setPY(double py) {
		this.py = py;
	}

	/**
	 * Sets the previous Z (vertical) value of the Particle
	 * 
	 * @param px
	 */

	public void setPZ(double pz) {
		this.pz = pz;
	}

	/**
	 * Sets whether the Particle data should be recorded by the Writer classes
	 * 
	 * @param recording
	 */

	public void setRecording(boolean recording) {
		this.recording = recording;
	}

	/**
	 * Sets whether the Particle is eligible for settling
	 * 
	 * @param settling
	 */

	public void setSettling(boolean settling) {
		this.settling = settling;
	}

	/**
	 * Sets the source location of the Particle
	 * 
	 * @param source
	 */

	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * Sets the T (time) coordinate of the particle
	 * 
	 * @param t
	 */

	public void setT(long t) {
		this.t = t;
	}

	/**
	 * Sets the U (east-west velocity) value of the Particle
	 * 
	 * @param u
	 */

	public void setU(double u) {
		this.u = u;
	}

	/**
	 * Sets the V (north-south velocity) value of the Particle
	 * 
	 * @param v
	 */

	public void setV(double v) {
		this.v = v;
	}

	/**
	 * Assigns the set of visited destinations
	 * 
	 * @param visited
	 */

	public void setVisited(Set<Long> visited) {
		this.visited = visited;
	}

	/**
	 * Sets the W (vertical velocity) value of the Particle
	 * 
	 * @param w
	 */

	public void setW(double w) {
		this.w = w;
	}

	/**
	 * Sets the original X coordinate of the particle
	 * 
	 * @param x
	 */

	public void setX0(double x0) {
		this.x0 = x0;
	}

	/**
	 * Sets the original Y coordinate of the particle
	 * 
	 * @param y
	 */

	public void setY0(double y0) {
		this.y0 = y0;
	}

	/**
	 * Sets the original Z coordinate of the particle
	 * 
	 * @param z
	 */

	public void setZ0(double z0) {
		this.z0 = z0;
	}
	
	/**
	 * Sets the X coordinate of the particle
	 * 
	 * @param x
	 */
	
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Sets the Y coordinate of the particle
	 * 
	 * @param y
	 */

	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Sets the Z coordinate of the particle
	 * 
	 * @param z
	 */

	public void setZ(double z) {
		this.z = z;
	}

	/**
	 * Returns a String representation of the Particle
	 * 
	 * @return
	 */

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(8);
		sb.append(id);
		sb.append("\t");
		sb.append(new Date(t));
		sb.append("\t");
		sb.append(z);
		sb.append("\t");
		sb.append(y);
		sb.append("\t");
		sb.append(x);
		sb.append("\t");
		sb.append(nf.format(u) + "," + nf.format(v) + "," + nf.format(w) + "\t");
		sb.append(source);
		return sb.toString();
	}

	/**
	 * Indicates if an error occurred while processing the Particle
	 * 
	 * @return boolean indicating if an error occurred while processing the
	 *         Particle
	 */

	public boolean wasError() {
		return error;
	}

	/**
	 * Indicates if NoData was ever encountered by the Particle
	 * 
	 * @return boolean indicating if NoData was ever encountered by the Particle
	 */

	public boolean wasNoData() {
		return hadNoData;
	}

	public int getTm() {
		return tm;
	}

	public void setTm(int tm) {
		this.tm = tm;
	}

	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}
}