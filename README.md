Conn4D
======

4-dimensional oceanic biophysical dispersal modelling

Connectivity research involves investigating the presence, strength and characteristics of spatiotemporal relationships between populations of organisms.  Typically, marine organisms release large numbers (often on the order of millions) of young (larvae), which are difficult to follow via physical tracking.  Instead, researchers have turned to numerical simulations, coupling models of ocean movement with larval behaviour.

The model described here uses oceanographic data (typically provided by ocean models such as HYCOM, BlueLink, POM or ROMS) to disperse artificially intelligent particles.  The fundamental physical processes at work are advection and diffusion, however the particles are able to respond in a flexible manner to other parameters as well, for example temperature, salinity, or prey fields.  Due to the object-oriented architecture of the model, various components can be changed, improved or modified without altering the base structure of the code.

The dispersal data can then be used in a variety of different ways:  e.g. to identify source-sink relationships between populations; to identify critical pathways or keystone habitats; to identify natural clusters or biogeographic regions; or to investigate the processes underlying population genetic structure (among many others).

--

Configuration

Configuration files contain parameter values that are specific to the machine where the code is being run.  This was done in preparation for the possibility that the code might be run in a server farm arrangement where the code would be run on many different machines independently, and then written to a single destination.

Example:  Configuration file
--
uname				          u
vname				          v
ufile				          ./AUS/Input/NetCDF/AUS_u_2005.nc
vfile				          ./AUS/Input/NetCDF/AUS_v_2005.nc
velocityType  	      IANN
latName			          Latitude
lonName			          Longitude
kName				          Depth
tName				          Time
negOceanCoord	        false
polyFileName	        ./AUS/Input/Shapefiles/AA_mesh_g.shp
polyKey			          POLYNUM
negPolyCoord	        false
landFileName	        ./AUS/Input/Shapefiles/AA_Land_Simple.shp
landKey			          ID
negLandCoord	        false
vertFile			        None
bathymetryFileName		None	
trajOutputDir			    ./AUS/Output
--
End of example


WARNING:  The configuration variable names are case sensitive.  The variable data are read into the code via reflection.  In other words, the text provided here is used to look up the variable within the code.  Java is a case-sensitive language, and therefore these variable names must be as well.

--

Configuration variables

uname/vname:  

This is the name (String) of the east-west/north-south velocity variable in the data file(s).  This has been necessary since it is possible for the name of the variable to change between oceanographic data sets, or even over different versions of the same data set.  A string must be supplied so that the code is able to identify which variable it should be reading.

ufile/vfile

This is the name (String) of the file containing the east-west/north-south velocity data.  It tells the code where to look to read in the velocity data.  Different forms of readers (e.g. ListReader, DirectoryReader) may be able to accept different arguments.

velocityType (vestigial)

This identifier was used to distinguish between interannual type runs and climatological runs.  Interannual data refers to data that reflects actual ‘on the ground’ conditions.  Climatological data is averaged in some manner over time – e.g. an ‘average’ January 1st.  Interannual runs are synchronized using a timestamp (i.e. java.util.Date).  Date is not suitable as a means of synchronizing climatological data (e.g. there is no ‘year’).  In its original form, the code used climatological data, but has not retained that capability – instead, interannual data has been used.  However, climatological-type data may be used again in the future if paleoceanographic data emerge, and so this variable has been left in the configuration file.

latName/lonName/kName/tName
		
This is the name (String) of the latitude/longitude/depth/time variable in the (velocity) data file(s).  As with the velocity variable names, the name of the variable can change between oceanographic data sets, or over different versions of the same data set.  A string must be supplied so that the code is able to identify which variable it should be reading.

negOceanCoord

This is used to indicate if the ocean current data is stored in association with negative longitude values.  Most oceanographic data sets (e.g. HYCOM) use positive coordinate values, and therefore this flag typically should not change.

polyFileName

The name of the file containing the settlement polygons.  This is typically an ESRI shapefile, although future work may also allow the use of geodatabase entries.  This is necessary to provide a settling environment for the particles (or to identify what they are passing over).

polyKey

This entry is used to identify the field within the settlement polygon shapefile that contains the unique key for the set.  This field is used by the Habitat class to extract the unique index value of the polygon that a particle is intersecting, which is then written to the trajectory and settlement files.

negPolyCoord

This identifies if the settlement file uses negative longitude coordinates.  This is important because the particle position in the model is stored as positive coordinates (longitude > 0).  If the settlement file is in negative coordinates, then the particle coordinates must be converted on the fly to negative coordinates in order to test for intersections properly.  The model does not project the settlement file into positive coordinates.  In future versions, we anticipate that the coordinate referencing and projection matching will be handled more explicitly.

landFileName

The name of the file containing the land mask information.  This information is used to identify areas where particles cannot travel, and intersecting behaviour is treated accordingly – e.g. bouncing or halting.

landKey

This entry is used to identify the field within the land mask shapefile that contains the unique key for the set.  This field is not strictly needed, but since the land mask information and settlement require the same sorts of spatial searching, the same class (Habitat_Shapefile) is used for both.  Future versions will likely distinguish between the two types in a better way.

negLandCoord

This identifies if the land mask file uses negative longitude coordinates.  This is important because the particle position in the model is stored as positive coordinates (longitude > 0).  If the settlement file is in negative coordinates, then the particle coordinates must be converted on the fly to negative coordinates in order to test for intersections properly.  The model does not project the land mask file into positive coordinates.  In future versions, we anticipate that the coordinate referencing and projection matching will be handled more explicitly.

trajOutputDir

Identifies the root directory where the output data should be stored.  The files are stored within folders corresponding to the release dates.

Optional variables
vertFile

This is the name (String) of the file containing information on the vertical movement behaviour of the particle over time.  The format of this file is dependent on which implementation of the VerticalMigration class being used.

bathymetryFileName

The name of the file containing bathymetry information.  This information is used to identify the lower limits of particle travel, and could also be used for additional effects such as deposition or other benthic interactions.

--

Parameters

Parameter files contain information that is used by the model run as a whole.  These are not machine-specific, and are shared across the problem set.  They are not modified by the model process, but are not currently declared as final since their values are read in via reflection (albeit only once).

Example:  Parameter file
--
idim   			          3		
h				              7200		
hUnits				        Seconds	
minTime			          01/25/2005	
minTimeUnits			    Date		
maxTime			          01/30/2006
maxTimeUnits			    Date				
relSp				          30
relSpUnits			      Days					
polyCheckStart		    1
polyCheckStartUnits 	Days					
polyChkFreq			      1
polyChkFreqUnits	    Days		
relDuration			      60  
relDurationUnits	    Days
outputFreq			      1
outputFreqUnits		    Days	
vmgrt				          false				
relFileName			      ./AUS/Input/ShapeFiles/NBNW_mesh.shp
mrate				          0.083333333
mortalityType			    Exponential
settlementType		    FloatOver
outputFolder			    Pilot
--
End of example

idim

This sets the number of (raster) cells around the focal cell that are read during velocity interpolation.  Interpolation is currently implemented using a bicubic spline.  For example, an idim of 3 would interpolate using a 7 x 7 cell environment.

For true 3D or space-time interpolation (4D) a tricubic or quadricubic spline can be used respectively.

h

This is the minimum integration time step - the minimum interval over which integration over the velocity field takes place.  For more detail on what the minimum integration time step does, please refer to the Process Walkthrough – Movement_RK4 section.

hUnits, <any>Units

This specifies the time units being used by the associated variable.  For synchronization, all time-related values are converted to milliseconds, corresponding to Java’s internal time stamp system.  Note, Java’s zero time stamp is referenced to January 1st, 1970, whereas HYCOM references to January 1st, 1900.  Time conversions are handled by lagrange.utils.TimeConvert.  Eligible string values are (case-insensitive):

Days, Day, D
Hours, Hour, Hrs, Hr, H
Minutes, Minute, Mins, Min, M
Seconds, Second, Secs, Sec, S
Date (formatted as M/d/yyyy)

minTime/maxTime

The beginning/ending time of the simulation.  As currently implemented, this value is converted to a millisecond value compatible with java.util.Date.  For climatological-type simulations, this will need to be interpreted differently (e.g. 1-365).  The model run ends if the simulation time value is equal to or greater than maxTime.

Note: maxTime is the time of the final termination of the model run.  In other words, if releases are scheduled to run every 30 days, and the maxTime is set as January 1st, the last release date will be no later than December 1st.  <Can the releases themselves continue on?>  <Also, please standardize what constitutes a ‘release’ ** maybe use the term ReleaseSet for the Collection?>

relSp

Equal and even temporal spacing for sets of Releases.  Variable release schedules have not been implemented.

polyChkStart

Strict onset of settling eligibility.  In other words, the time period after which particles are eligible for settling.  Particles are eligible for settling if they are equal to or greater than polyChkStart.  More complicated implementations of settling can be handled through the Settlement interface.

polyChkFreq

Identifies the time frequency of checking for settling.  For example, every 2 days, a particle checks to see if it can settle. 

relDuration

This is equivalent to the Pelagic Larval Duration (PLD), specifying the maximum amount of time that a particle can survive in the water column.  At or beyond this time interval (equal to or greater than), particles are no longer recorded.

outputFreq

Specifies the interval at which results are recorded to the output files.  For example, writing results for every 1 day of simulation time.

vmgrt

Flags whether active vertical migration behaviour is being used by the model.  If this parameter is not included, the default value is false.

relFileName

This specifies the file name (String) of the file containing the release locations, numbers and parameters.  For detailed information on the form of the release file, refer to the Release File section.

mrate

Specifies a single mortality rate to be used for eliminating particles.  Typically this value is used in association with an exponential decrease in survivorship, i.e. e-z, where z is the mortality rate and e is Euler’s number.  Other forms of mortality are also possible (e.g. Weibull), but currently, the program code must be changed manually.

mortalityType 

Identifies the form of mortality to be used by the simulation.  The default value is Exponential, however Weibull is also available as an option (although the parameters must be manually coded at present).

settlementType

This parameter indicates the form of settlement (Settlement implementation) used by the model.  Current options include Simple and FloatOver.  With SimpleSettlement, once particles encounter suitable habitat, and are eligible for settling, they settle and stop.  With FloatOver settlement, particles settle according to a given probability value.  The probability value is specified in the Habitat shapefile.

outputFolder

The file name (String) identifying the subfolder beneath the configuration file’s trajOutputDir path where the results are to be stored.  This was added since different machines could theoretically have different storage areas for the data, but a folder structure to organize the results would still be required.

--

Release Files

Release files can take one of two forms.  An older, text-based version, and a newer shapefile-based version.  Shapefile versions support a variety of geometries – e.g. point, line midpoint, polygon centroid, or random locations within a polygon or along a line.  The text-based version only supports releases from a point location.  There are future plans to provide support for geodatabase files.


Example:  Text-based release file

1   20.59288812630   92.32353086950   5   1000   0   0   1   MGI-1
2   20.25374702430   92.46184104630   5   1000   0   0   1   MGI-2
3   20.14612251940   92.47329554020   5   1000   0   0   1   MGI-3
4   20.25141220570   92.58835253210   5   1000   0   0   1   MGI-4
5   20.15415628700   92.59572373350   5   1000   0   0   1   MGI-5
6   20.01633268780   92.72403525910   5   1000   0   0   1   MGI-6
7   19.89402283330   92.69791562040   5   1000   0   0   1   MGI-7
8   20.01791264550   92.83083993750   5   1000   0   0   1   MGI-8
9   19.88497574650   92.82627457290   5   1000   0   0   1   MGI-9
10  19.79443617790   92.75799675900   5   1000   0   0   1   MGI-10
…

  End of example

 
The first column contains identifiers.  Currently these numbers are not used.  The model uses its own internal indexing for processing, and the output files are associated with the final column.  

The second and third columns contain latitude and longitude values respectively.

The fourth column identifies the depth at which the particles are situated (in meters).

The fifth column specifies the number of particles in the release set for the given source population.

Columns six through eight are not used, and were originally included to maintain backwards compatibility with the original Fortran-based code.  This compatibility is now broken.  The text-file format needs to be updated, however priority has been given to developing the shapefile and geodatabase format release files instead.

Column nine specifies the name associated with the release set, and is used to name the output files.

WARNING:  If using a text-based release file, all columns must be present and in the appropriate order.

The tabular information associated with a shapefile looks like the following:

Example:  Shapefile-based release file (table)

OBJECTID	POLYNUM	NPART	DEPTH	FNAME
5257	5257	1000	5.00000000000	A5257
5258	5258	1000	5.00000000000	A5258
5259	5259	1000	5.00000000000	A5259
5310	5310	1000	5.00000000000	A5310
5311	5311	1000	5.00000000000	A5311
5312	5312	1000	5.00000000000	A5312
5313	5313	1000	5.00000000000	A5313
…

	End of example

Columns one and two are used for internal referencing and post-processing, and are not used by the model code.  NPART corresponds to column five of the text file (number of particles), DEPTH corresponds to column four of the text file, and FNAME corresponds to column nine.  Unlike the text file, the order of the fields is not important.  Specifying the appropriate field names to be used (when reading from a Shapefile) is currently handled by changing the program code manually.

Shapefiles

Typically, the main shapefiles (or spatial data) required are the land mask and settlement file.  If the particles engage in more sophisticated behaviour, additional spatial data may be necessary.

Land mask data is usually used to ‘reflect’ particles that intersect its boundaries back into the water column.  Because of the large number of intersection operations required, these data are usually weeded/generalized to a resolution that balances accuracy and computational burden.  Generalization of the land mask is currently performed in advance using ArcGIS’s Simplify Polygon command.

Settlement data is used to identify areas suitable for settlement (or alternatively, as a reference for areas that the particles are passing over).  These data are also generated in advance in ArcGIS using a combination of buffering and using Thiessen Polygons to develop segmented regions.  Typically the buffer for reef-type environments is 5 km from the centre, and the segments are split using a tolerance value of 10 km.  Current work uses a mesh-type habitat framework to bin the particle tracks.  Technically, settlement could be deduced through post-processing on the particle tracks, however this will require heavy computation.
 
Running the model

Assuming that the necessary input files are available, and the parameters have been properly configured, the entry point to running the model is through the main method of the JB4 class.

The model has a large number of complex dependencies on external libraries (e.g. GeoTools), and therefore the use of the Eclipse Interactive Development Environment (IDE) has been instrumental in keeping them organized and facilitating compiling.  In cases where the code needs to be run in a command line environment (e.g. Linux on an HPC), the code has been deployed using a .jar file assembled in Eclipse.  For information specific to running within a Linux-HPC environment, refer to the Running in an HPC environment section <to be added>.

Setting the virtual machine parameters

By default, Java’s maximum memory capacity is 64 Mb of memory – and is generally too small to run the model properly.  To increase the amount of memory available to be used, the following arguments must be added when initializing the Java Virtual Machine (JVM).  For more information, click here.

-Xss<size> -Xmx<size> -Xmn<size>

<size> is a string of numbers with k, m or g (case insensitive) at the end, signifying kilobytes, megabytes or gigabytes respectively.

-Xss sets the stack size for each thread.  The stack size limits the number of threads, however too large of a stack size will result in running out of memory as each thread is allocated more memory than it needs.

-Xmx is the maximum heap size (maximum amount of RAM to be used by the heap).  Note:  Java may allocate memory for other resources (e.g. a stack for each thread), and therefore the total memory consumption can exceed –Xmx.

-Xmn is the size of the heap for the young generation (the ‘young generation’ refers to newly created objects that have not passed through a number of garbage collections).  The default setting for this is typically too low, resulting in a high number of minor garbage collections. Setting this setting too high can cause the JVM to only perform major (or full) garbage collections. These usually take several seconds and are extremely detrimental to the overall performance of your server. This setting should be kept below half of the overall heap size to avoid this situation.

-Xms can be used to specify the initial heap size if desired.

Other parameters that can be added include:

-XX:+UseParallelGC – enables the young generation parallel scavenge collector.

-XX:+UseAdaptiveSizePolicy – automatically sizes the young generation and chooses an optimum survivor ratio to maximize performance.

Command line arguments

The model also accepts two command line arguments.  The first is a string containing the path and filename for the parameter file to be used by the model run.  The second is a string containing the path and filename of the configuration file to be used for this instance of the JVM.

In Eclipse, VM arguments and command line arguments are entered in the Run Configurations dialog box (Run menu, or drop down arrow beside the ‘play’ button).


Example:  Command line execution
--
java -Xss1024k -Xmx24g -Xmn512m -XX:+UseParallelGC -XX:+UseAdaptiveSizePolicy  -jar njb.jar ./AUS/Input/ParameterFiles/AUS_pilot.prm ./AUS/Input/Configuration/AUS_2005.cfg
--
End of example


 
Process Walkthrough

Entry point:  JB4

1)	The program initially looks for the parameter and configuration files as arguments supplied to the command line or through Eclipse’s Run Configurations.  If the configuration file is not present or cannot be found, the program terminates.  If the parameter file cannot be found, the program will attempt to continue using a default parameter file, printing a warning to that effect.

2)	ReleaseRunner is parameterized using the configuration file.  ReleaseRunner performs the work of generating (through a ReleaseFactory) and running the individual Release threads.  ReleaseFactory (through ReleaseRunner) sets up the output writers (e.g. TrajectoryWriter, MatrixWriter, DistanceWriter), and also initializes Movement, Habitat (land and settlement areas), and VelocityReaders.

3)	The start, end and release spacings are converted to a millisecond time stamp (Java’s time reference).


For each release site identified in the ReleaseFile

4)	Construct a Datagram containing the parameters pertaining to that particular Release set (i.e. a release polygon or point).  The Datagram construct was developed in preparation for the possibility that requests would need to be serialized and sent to a remote JVM for processing.  Parameters of the datagram are set by the ReleaseFile as well as the GlobalParameters class.

5)	ReleaseRunner receives the Datagram, and processes individual Releases, generating a single thread per release.  If effective migration is being used (recommended), the program discards particles that would not have made it to settling age.  This is achieved through binomial sampling, where n is the total number of individuals, and p is the probability that they make it to settling age.

6)	A CountDownLatch (java.util.concurrent) is constructed to ensure that all impending threads will be processed in the next block before continuing on (avoiding thread loss, deadlocks, mixed output etc.).


For each Release in the set of Releases (that survive to settlement if using effective migration)


7)	Generate a new Particle instance.  The Particle represents an individual organism/larvae.

8)	Assign an ID to the particle

9)	Assign properties to the particle based on the Datagram


While the particle has not exceeded the duration of the release (PLD), spaced by the minimum integration time step (h):

10)	Update the time reference (we do not start at time 0 because this information is already recorded in the release file).

11)	If effective migration is being used, only apply mortality after exceeding the first day of settling (since they have been pre-filtered up to this point).

12)	Apply the movement routine (for details, see Movement section).

13)	Apply turbulent diffusion (for details, see Turbulence section).

14)	Calculate the distance travelled (along the surface of a sphere).

15)	Check to see if the particle has encountered a land boundary.  If so, then apply reflection (for details, see the Habitat section).

16)	Check to see if the particle can settle (for details, see the Habitat section).

17)	 If the particle time has exceeded the output frequency or if it can settle, then write to output.  We write at the first settling opportunity in order to avoid having the particle skipping over habitat, or continuing on and settling in a different location (potentially even outside of a settlement area).

18)	If the particle is finished for some reason (lost, error, killed), then break.

In all cases when the Release is finished, the CountdownLatch is decremented, and the Particle’s Movement and TurbVar components are closed.


Movement
	
MovementRK4 uses the Cash-Karp method for 4th-order Runge-Kutta integration across the velocity field.  The Cash-Karp method uses six function evaluations to calculate fourth and fifth-order accurate solutions to ordinary differential equation (ODE) problems.

At each function evaluation (total of 6):

1)	Get the u and v velocities at the Particle’s position (x,y,z,t) using the VelocityReader.
2)	If the Particle is near a No Data value, this should be flagged.
3)	If the velocity values are Null, this means the Particle is outside of the velocity field domain, and has therefore been lost (flag and break).
4)	Apply the appropriate Butcher tableau weightings, and calculate the resulting displacement

Note:  the velocities are re-evaluated at each incremental function evaluation.  The final displacement distances (dx and dy) are a function of the weighted velocity values.  The displacement distance (spherical) is converted to new latitude and longitude values by Utils.latlon.

VelocityReader typically obtains velocity values from NetCDF files, but also performs interpolation on the fly.  Interpolation is carried out using a quadricubic spline (algorithm developed by Thomas Flanagan).

Why are interpolation and integration important?

To illustrate the difference that interpolation and integration make, consider the following example of a particle on the illustrated flow field.

<to be added>


Turbulence

Turbulent diffusion is currently carried performed using equations 8 and 9 from Dimou, K.N. and Adams, E.E. 1993 - Estuarine and Coastal Shelf Science 37:99-110. A Random-walk, Particle Tracking Model for Well-Mixed Estuaries and Coastal Waters.

The equation is  

Where delta t = h, the minimum integration time step, K is the coefficient of diffusivity, and N(0,1) is a random number generated from the normal distribution with a mean of 0 and unit variance (variance=1).  K is currently given a blanket value of 0.3 based on empirical measurements of larval dispersion in fish, however efforts are under way to incorporate spatially-varying K values into the model.

Habitat

The Habitat interface handles interactions of the Particles with land and settlement habitat (land can be considered as a ‘form’ of Habitat).  The primary function of the interface is to provide methods for fast spatial searching of point-in-polygon intersections.  Currently this is handled by constructing a fixed spatial search index in advance (habitats are effectively static during a given model run) and then querying the spatial envelopes stored within the index.  The Java Topology Suite (JTS)’s STRtree is used to perform the initial query.  I have not delved into the inner workings of STRtree, however one potential area of enhancement may come through the use of splay trees for searching, as suggested by Cobb et al. (also see Zhang), since many of the searches are likely to be highly autocorrelated.

Random number generation

Random number generation is typically carried out using the MersenneTwister64 class of CERN’s Colt package (v 1.2).

 
Output

Writing to output is currently handled by various Writer classes (e.g. TrajectoryWriter, MatrixWriter, DistanceWriter).  If a Particle is active or newly settled, its information is persisted using these classes.  Currently, these classes are implemented using synchronized methods in order to avoid mixed/garbled output.  In other words, output is written one line at a time, documenting the current state of a given Particle object.  Because of the synchronized nature of these code blocks, they presently represent a large bottleneck for processing, and this has been verified using code profiling.  Potential options include parallel file write operations, or submitting batch requests to a RDBMS.

Trajectory file

The trajectory file contains discrete point locations (which can be converted into linear paths) detailing the positions of Particles over time.  A section of a text-based file is provided below:

Example:  trajectory output (actual files have greater precision for lon, lat, and dist)

ID   TIME	            DURATION   DEPTH   LON      LAT     DIST   STATUS   NODATA
1    2004-05-31 00:00:00   1.0        5.0     94.7780  5.8514  7655.6 I	    false
2    2004-05-31 00:00:00   1.0        5.0     94.9008  5.9506  5315.1 I	    false
3    2004-05-31 00:00:00   1.0        5.0     94.8220  5.8426  6956.3 I        false
4    2004-05-31 00:00:00   1.0        5.0     94.8211  5.7848  7841.5 I        false
6    2004-05-31 00:00:00   1.0        5.0     94.8549  5.7835  6913.1 I        false
5    2004-05-31 00:00:00   1.0        5.0     94.8826  5.8891  6115.4 I        false
7    2004-05-31 00:00:00   1.0        5.0     94.7840  5.7744  8275.0 I        false
2    2004-06-01 00:00:00   2.0        5.0     94.7403  5.6247 12337.0 I        false
1    2004-06-01 00:00:00   2.0        5.0     94.6888  5.5038 14397.6 I        false
9    2004-05-31 00:00:00   1.0        5.0     94.7776  5.8501  8078.9 I        false
8    2004-05-31 00:00:00   1.0        5.0     94.7825  5.8045  7834.0 I	    false
4    2004-06-01 00:00:00   2.0        5.0     94.7738  5.4588 14144.6 I        false
6    2004-06-01 00:00:00   2.0        5.0     94.8040  5.5265 12056.9 I        false
3    2004-06-01 00:00:00   2.0        5.0     94.7254  5.5743 12524.9 I        false
5    2004-06-01 00:00:00   2.0        5.0     94.7409  5.6454 11671.5 I        false
…

End of example

ID

The internal ID of the Particle.  Acts as a unique identifier for a Release within a given Release set.

Duration

Convenience field, indicating the length of time (typically converted to days) that the Particle has been in the water column.

Depth

The depth (z) of the Particle.  This value should stay constant unless using vertical migration or full 3D movement (not implemented yet).

Lon/Lat

Indicates the position of the particle (decimal degrees on a sphere with radius 6372795.477598 meters – quadratic mean radius of Earth).

Dist

Distance travelled by the Particle (m).

 
Status

State of the particle.  The following codes apply:

I		In transit
X		Error
L		Lost
M		Dead 	(should not appear, since dead particles are typically not recorded)
S<num>		Settled	num indicates where the Particle Settled (or was eligible to settle)	
NoData

Did the Particle pass through a large region of NoData  (i.e. velocity was inferred)?

Settlement file (.set)

This is a subset of the .trj files, only containing information on settlement events.  These files have the same format as the .trj files, however the STATUS and NODATA fields have been removed, and a LOCATION field has been added that records the area where the particle was capable of settling.

Summary file (.sum)

These files contain summarized settlement counts for a single release source to multiple destinations.  The data is stored as a sparse vector of key/value pairs.  This information can be reconstructed from the .trj and .set files, but the information can be collated quickly and without significant overhead while the process is running.

Example:  SMT-2379.sum file output

{2381=9, 2383=166, 2388=49, 2390=1, 2393=28, 2395=19}

End of example

The value to the left of the equal sign is the unique settlement destination ID (obtained from the polyKey field of the Settlement file).  The value to the right of the equal sign is the number of individuals from the source population (given by the name of the file – in this case SMT-2379) settling in the destination population (e.g. 2381).  Currently this information is converted back into transition matrix form using a matrix assembly script.  This type of lookup strategy was originally put in place to maintain backwards compatibility with the original Fortran code, but future versions will likely involve bypassing the unique index and performing lookup using a single unique label field.

Distance file (.dst)

These files have the same format as .sum files but rather than recording counts of individuals arriving in a particular destination, instead the average distance travelled by particles is recorded.  This information can be re-constructed from the .trj and .set files.

Post-processing

The chief advantage of persisting the particle tracks is that is possible to manipulate and display the information in a wide variety of ways.  For example, particle positions can be converted into a density surface to yield probability dispersal kernels. 

Much of the post-processing for visualization is carried out using ESRI’s ArcGIS software.  ArcGIS makes extensive use of databases, and consequently much of the data is ultimately ingested into some form of database management system.  Other analyses, particularly matrix processing, is carried out in Matlab, although many of these processing routines are now being converted into Python/NumPy scripts.  

How is output used?

The output can be used in a variety of ways.  The first, and most basic use is to display the particle positions to get a general sense of dispersal patterns in different geographic areas and times.

Reference papers

Cowen R.K., Lwiza K.M.M., Sponaugle S., Paris C.B. and Olson D.B. 2000. Connectivity of marine populations: Open or closed? Science 287: 857-859.
Cowen R.K., Paris C.B. and Srinivasan A. 2006. Scaling of Connectivity in Marine Populations Science 311: 522-527.
Foster N.L., Paris C.B., Kool J.T., Baums I.B., Stevens J.R., Sanchez J.A., Bastidas C., Agudelo C., Bush P., Day O., Ferrari R., Gonzalez P., Gore S., Guppy R., McCartney M.A., McCoy C., Mendes J., Srinivasan A., Steiner S., Vermeij M.J.A., Weil E. and Mumby P.J. Connectivity of Caribbean coral populations: complementary insights from empirical and modelled gene flow. Molecular Ecology 21: 1143-1157.
Kool J.T., Paris C.B., Andréfouët S. and Cowen R.K. 2010. Complex migration and the development of genetic structure in subdivided populations: an example from Caribbean coral reef ecosystems. Ecography 33: 597-606.
Kool J.T., Paris C.B., Barber P.H. and Cowen R.K. 2011. Connectivity and the development of population genetic structure in Indo-West Pacific coral reef communities. Global Ecology and Biogeography 20: 695-706.
Paris C.B., Chérubin L.M. and Cowen R.K. 2007. Surfing, spinning, or diving from reef to reef: effects on population connectivity. Marine Ecology Progress Series 347: 285-300.
