package org.dcm4che2.data;

import java.util.Date;

/**
 * @author gunter zeilinger(gunterze@gmail.com)
 * @version $Revision: 5542 $ $Date: 2007-11-26 14:16:23 +0100 (Mon, 26 Nov 2007) $
 * @since Jun 27, 2005
 *
 */
public class DateRange {

	private final Date start;
	private final Date end;

	public DateRange(Date start, Date end) {
		this.start = start;
		this.end = end;
	}

	public final Date getStart() {
		return start;
	}

	public final Date getEnd() {
		return end;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (start != null) sb.append(start);
		sb.append("-");
		if (end != null) sb.append(end);
		return sb.toString();
	}

}

