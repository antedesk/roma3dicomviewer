package org.dcm4che2.data;

import org.dcm4che2.util.TagUtils;

/**
 * @author gunter zeilinger(gunterze@gmail.com)
 * @version $Reversion$ $Date: 2007-11-26 14:16:23 +0100 (Mon, 26 Nov 2007) $
 * @since Sep 3, 2005
 *
 */
abstract class AbstractDicomElement implements DicomElement {

	protected static final int TO_STRING_MAX_VAL_LEN = 64;
	protected transient int tag;    
	protected transient VR vr;    
	protected transient boolean bigEndian;

	public AbstractDicomElement(int tag, VR vr, boolean bigEndian) {
		this.tag = tag;
		this.vr = vr;
		this.bigEndian = bigEndian;
	}

	@Override
	public int hashCode() {
		return tag;
	}

	public final boolean bigEndian() {
		return bigEndian;
	}

	public final int tag() {
		return tag;
	}

	public final VR vr() {
		return vr;
	}

	@Override
	public String toString() {
		return toStringBuffer(null, TO_STRING_MAX_VAL_LEN).toString();
	}

	public StringBuffer toStringBuffer(StringBuffer sb, int maxValLen) {
		if (sb == null)
			sb = new StringBuffer();
		TagUtils.toStringBuffer(tag, sb);
		sb.append(' ');
		sb.append(vr);
		sb.append(" #");
		sb.append(length());
		sb.append(" [");
		appendValue(sb, maxValLen);
		sb.append("]");
		return sb;
	}

	protected abstract void appendValue(StringBuffer sb, int maxValLen);

	public DicomElement bigEndian(boolean bigEndian) {
		if (this.bigEndian == bigEndian)
			return this;
		toggleEndian();
		this.bigEndian = bigEndian;
		return this;
	}

	protected abstract void toggleEndian();
}
