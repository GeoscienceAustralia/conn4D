package au.gov.ga.conn4d;

import java.text.NumberFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Generic particle class.
 */

public class Particle implements Cloneable {

	private long id;
	private double x, y, z;
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
	 */

	public boolean canSettle() {
		return settling;
	}

	/**
	 * Returns an independent copy of the class instance
	 */

	public Particle clone() {
		Particle p = new Particle();
		p.id = this.id;
		p.x = this.x;
		p.y = this.y;
		p.z = this.z;
		p.px = this.px;
		p.py = this.py;
		p.pz = this.pz;
		p.x0 = this.x0;
		p.y0 = this.y0;
		p.z0 = this.z0;
		p.u = this.u;
		p.v = this.v;
		p.w = this.w;
		p.t = this.t;
		p.birthday = this.birthday;
		p.tm = this.tm;
		p.i = this.i;
		p.j = this.j;
		p.k = this.k;
		p.competencyStart = this.competencyStart;
		p.settling = this.settling;
		p.dead = this.dead;
		p.error = this.error;
		p.finished = this.finished;
		p.recording = this.recording;
		p.nodata = this.nodata;
		p.nearNoData = this.nearNoData;
		p.hadNoData = this.hadNoData;
		p.distance = this.distance;
		p.source = this.source;
		p.destination = this.destination;
		p.visited = new HashSet<Long>(this.visited);
		p.comments = this.comments;
		return p;
	}

	/**
	 * Performs deep comparison of fields
	 */

	public boolean deepEquals(Particle p) {
		boolean equals = true;
		equals &= p.id == this.id;
		equals &= p.x == this.x;
		equals &= p.y == this.y;
		equals &= p.z == this.z;
		equals &= p.px == this.px;
		equals &= p.py == this.py;
		equals &= p.pz == this.pz;
		equals &= p.x0 == this.x0;
		equals &= p.y0 == this.y0;
		equals &= p.z0 == this.z0;
		equals &= p.u == this.u;
		equals &= p.v == this.v;
		equals &= p.w == this.w;
		equals &= p.t == this.t;
		equals &= p.birthday == this.birthday;
		equals &= p.tm == this.tm;
		equals &= p.i == this.i;
		equals &= p.j == this.j;
		equals &= p.k == this.k;
		equals &= p.competencyStart == this.competencyStart;
		equals &= p.settling == this.settling;
		equals &= p.dead == this.dead;
		equals &= p.error == this.error;
		equals &= p.finished == this.finished;
		equals &= p.recording == this.recording;
		equals &= p.nodata == this.nodata;
		equals &= p.nearNoData == this.nearNoData;
		equals &= p.hadNoData == this.hadNoData;
		equals &= p.distance == this.distance;
		equals &= p.source == this.source;
		equals &= p.destination == this.destination;
		
		if(p.visited==null && this.visited==null){}
		else if (p.visited.size() != this.visited.size()) {
			equals = false;
		} else {
			Iterator<Long> it1 = p.visited.iterator();
			Iterator<Long> it2 = this.visited.iterator();
			while (it1.hasNext()) {
				equals &= it1.equals(it2);
			}
		}

		equals &= p.comments.equals(this.comments);
		return equals;
	}

	/**
	 * Retrieve the age of the particle (milliseconds)
	 */

	public long getAge() {
		return this.t - this.birthday;
	}

	/**
	 * Retrieves the birth timestamp of the particle (milliseconds)
	 */

	public long getBirthday() {
		return birthday;
	}

	/**
	 * Retrieves comment string (for storing general information)
	 */

	public String getComments() {
		return comments;
	}

	/**
	 * Retrieves the duration after which the Particle becomes competent to
	 * settle
	 */

	public long getCompetencyStart() {
		return competencyStart;
	}

	/**
	 * Retrieve the current coordinates of the particle
	 */

	public double[] getCoords() {
		return new double[] { x, y, z };
	}

	/**
	 * Retrieve the settlement destination of the Particle
	 */

	public String getDestination() {
		return destination;
	}

	/**
	 * Retrieve the distance traveled by the Particle (meters)
	 */

	public double getDistance() {
		return distance;
	}

	/**
	 * Retrieve the unique identifier of the particle
	 */

	public long getID() {
		return id;
	}

	/**
	 * Identify whether the particle encountered a NoData value in the velocity
	 * field.
	 */

	public boolean getNodata() {
		return nodata;
	}

	/**
	 * Retrieve the original X Coordinate of the particle
	 */

	public double getX0() {
		return x0;
	}

	/**
	 * Retrieve the Previous Y Coordinate of the particle
	 */

	public double getY0() {
		return y0;
	}

	/**
	 * Retrieve the Previous Z Coordinate of the particle
	 */

	public double getZ0() {
		return z0;
	}

	/**
	 * Retrieve the Previous X Coordinate of the particle
	 */

	public double getPX() {
		return px;
	}

	/**
	 * Retrieve the Previous Y Coordinate of the particle
	 */

	public double getPY() {
		return py;
	}

	/**
	 * Retrieve the Previous Z Coordinate of the particle
	 */

	public double getPZ() {
		return pz;
	}

	/**
	 * Retrieves the release source ID for the Particle
	 */

	public String getSource() {
		return source;
	}

	/**
	 * Retrieve the Time Coordinate of the particle
	 */

	public long getT() {
		return t;
	}

	/**
	 * Retrieve the last E-W velocity of the particle
	 */

	public double getU() {
		return u;
	}

	/**
	 * Retrieve the last N-S velocity of the particle
	 */

	public double getV() {
		return v;
	}

	/**
	 * Retrieve the set of visited destinations
	 */

	public Set<Long> getVisited() {
		return visited;
	}

	/**
	 * Retrieve the last vertical velocity of the particle
	 */

	public double getW() {
		return w;
	}

	/**
	 * Retrieve the X coordinate of the particle
	 */

	public double getX() {
		return x;
	}

	/**
	 * Retrieve the Y coordinate of the particle
	 */

	public double getY() {
		return y;
	}

	/**
	 * Retrieve the Z Coordinate of the particle
	 */

	public double getZ() {
		return z;
	}

	/**
	 * Indicates if the Particle is dead
	 */

	public boolean isDead() {
		return dead;
	}

	/**
	 * Indicates if the Particle has completed its run
	 */

	public boolean isFinished() {
		return finished;
	}

	/**
	 * Indicates if the particle was lost - e.g. through exiting boundaries
	 */

	public boolean isLost() {
		return lost;
	}

	/**
	 * Indicates if the Particle encountered a NoData value in the velocity
	 * field
	 */

	public boolean isNearNoData() {
		return nearNoData;
	}

	/**
	 * Indicates whether the particle date should be recorded (by the Writers)
	 */

	public boolean recording() {
		return recording;
	}

	/**
	 * Sets the birthday (millisecond time) of the Particle
	 */

	public void setBirthday(long birthday) {
		this.birthday = birthday;
	}

	/**
	 * Sets the value of a general information String attached to the Particle
	 */

	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * Sets the duration after which the Particle is eligible to settle
	 * (milliseconds)
	 */

	public void setCompetencyStart(long competencyStart) {
		this.competencyStart = competencyStart;
	}

	/**
	 * Sets the Particle as dead
	 */

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	/**
	 * Sets the settlement destination of the particle (text representation).
	 */

	public void setDestination(String destination) {
		this.destination = destination;
	}

	/**
	 * Sets the distance traveled by the Particle (meters)
	 */

	public void setDistance(double distance) {
		this.distance = distance;
	}

	/**
	 * Sets whether the particle encountered an error during processing
	 */

	public void setError(boolean error) {
		this.error = error;
	}

	/**
	 * Sets whether the Particle has finished its cycle and can be ended
	 * in a natural manner.
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
	 * @param pz
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
	 * @param x0
	 */

	public void setX0(double x0) {
		this.x0 = x0;
	}

	/**
	 * Sets the original Y coordinate of the particle
	 * 
	 * @param y0
	 */

	public void setY0(double y0) {
		this.y0 = y0;
	}

	/**
	 * Sets the original Z coordinate of the particle
	 * 
	 * @param z0
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
		sb.append(pz);
		sb.append("\t");
		sb.append(py);
		sb.append("\t");
		sb.append(px);
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