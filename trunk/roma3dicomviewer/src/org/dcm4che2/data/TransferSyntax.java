package org.dcm4che2.data;

import java.util.Hashtable;
import java.util.Map;

public class TransferSyntax
{

	public static final TransferSyntax ImplicitVRLittleEndian =
			new TransferSyntax("1.2.840.10008.1.2", false, false, false, false);

	public static final TransferSyntax ImplicitVRBigEndian =
			new TransferSyntax("1.2.840.113619.5.2", false, true, false, false);

	public static final TransferSyntax ExplicitVRLittleEndian =
			new TransferSyntax("1.2.840.10008.1.2.1", true, false, false, false);

	public static final TransferSyntax ExplicitVRBigEndian =
			new TransferSyntax("1.2.840.10008.1.2.2", true, true, false, false);

	public static final TransferSyntax DeflatedExplicitVRLittleEndian =
			new TransferSyntax("1.2.840.10008.1.2.1.99", true, false, true, false);

	public static final TransferSyntax NoPixelData =
			new TransferSyntax("1.2.840.10008.1.2.4.96", true, false, false, false);

	public static final TransferSyntax NoPixelDataDeflate =
			new TransferSyntax("1.2.840.10008.1.2.4.97", true, false, true, false);

	private static final Map<String,TransferSyntax> map =
			new Hashtable<String,TransferSyntax>();

	static {
		add(ImplicitVRLittleEndian);
		add(ImplicitVRBigEndian);
		add(ExplicitVRLittleEndian);
		add(ExplicitVRBigEndian);
		add(DeflatedExplicitVRLittleEndian);
		add(NoPixelData);
		add(NoPixelDataDeflate);
	}

	/**
	 * Add entry for private Transfer Syntax to be returned by {@link valueOf}.
	 * Necessary to decode DICOM Objects encoded with Private Transfer Syntax
	 * with Big Endian or/and Implicit VR encoding.
	 *
	 * @param ts entry for private Transfer Syntax
	 */
	public static void add(TransferSyntax ts) {
		map.put(ts.uid, ts);
	}

	public static TransferSyntax remove(String tsuid) {
		return map.remove(tsuid);
	}

	public static TransferSyntax valueOf(String uid) {
		if (uid == null)
			throw new NullPointerException("uid");
		TransferSyntax ts = map.get(uid);
		return ts != null ? ts
				: new TransferSyntax(uid, true, false, false, true);
	}

	private final String uid;

	private final boolean bigEndian;

	private final boolean explicitVR;

	private final boolean deflated;

	private final boolean encapsulated;

	public TransferSyntax(String uid, boolean explicitVR, boolean bigEndian,
			boolean deflated, boolean encapsulated)
	{
		this.uid = uid;
		this.explicitVR = explicitVR;
		this.bigEndian = bigEndian;
		this.deflated = deflated;
		this.encapsulated = encapsulated;
	}

	public final String uid()
	{
		return uid;
	}

	public final boolean bigEndian()
	{
		return bigEndian;
	}

	public final boolean explicitVR()
	{
		return explicitVR;
	}

	public final boolean deflated()
	{
		return deflated;
	}

	public final boolean encapsulated()
	{
		return encapsulated;
	}

	public final boolean uncompressed()
	{
		return !deflated && !encapsulated;
	}

	/** Check to see if the transfer syntax is the same */
	@Override
	public  boolean equals(Object o2) {
		if( ! (o2 instanceof TransferSyntax) ) return false;
		return uid().equals(((TransferSyntax) o2).uid());
	}

	@Override
	public int hashCode() {
		return uid.hashCode();
	}
}

