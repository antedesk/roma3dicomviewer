package org.dcm4che2.data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Permette la creazione e la gestione di un oggetto che contiene un insieme di dicomObject
 * inoltre � possibile farne una gestione
 * 
 * @author gunter zeilinger(gunterze@gmail.com)
 * @version $Reversion$ $Date: 2010-01-15 11:18:54 +0100 (Fri, 15 Jan 2010) $
 * @since Sep 3, 2005
 */
public class SequenceDicomElement extends AbstractDicomElement {

	private static final long serialVersionUID = 3690757302122656054L;

	private transient List<Object> items;
	private transient DicomObject parent;

	public SequenceDicomElement(int tag, VR vr, boolean bigEndian, List<Object> items,
			DicomObject parent) {
		super(tag, vr, bigEndian);
		if (items == null)
			throw new NullPointerException();
		//        System.out.println("SequenceDicomElement tag/VR " + Integer.toHexString(tag) + " " + vr);
		//        Thread.dumpStack();
		this.items = items;
		this.parent = parent;
	}

	// used by ElementSerializer
	void setParentDicomObject(DicomObject parent) {
		this.parent = parent;
		for (int i = 0, n = items.size(); i < n; ++i) {
			Object item = items.get(i);
			if (item instanceof DicomObject) {
				((DicomObject) item).setParent(parent);
			}            
		}
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
		s.defaultWriteObject();
		s.writeInt(tag);
		s.writeShort(vr.code());
		s.writeBoolean(bigEndian);
		int size = items.size();
		s.writeInt(size);
		for (int i = 0; i < size; ++i) {
			Object item = items.get(i);
			if (item instanceof DicomObject) {
				s.writeObject(new ElementSerializer((DicomObject) item));
			} else {
				s.writeObject(item);
			}
		}
	}

	private void readObject(ObjectInputStream s) throws IOException,
	ClassNotFoundException {
		s.defaultReadObject();
		tag = s.readInt();
		vr = VR.valueOf(s.readUnsignedShort());
		bigEndian = s.readBoolean();
		int n = s.readInt();
		items = new ArrayList<Object>(n);
		for (int i = 0; i < n; ++i) {
			items.add(s.readObject());
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof SequenceDicomElement)) {
			return false;
		}
		SequenceDicomElement other = (SequenceDicomElement) o;
		return tag() == other.tag() && vr() == other.vr()
				&& items.equals(other.items);
	}

	public DicomElement share() {
		if (hasDicomObjects()) {
			for (int i = 0, n = items.size(); i < n; ++i) {
				((DicomObject) items.get(i)).shareElements();
			}
		}
		return this;
	}

	@Override
	protected void appendValue(StringBuffer sb, int maxValLen) {
		final int size = items.size();
		if (size != 0) {
			if (size == 1)
				sb.append("1 item");
			else {
				sb.append(size).append(" items");
			}
		}
	}

	@Override
	protected void toggleEndian() {
		if (!hasDicomObjects()) {
			for (int i = 0, n = items.size(); i < n; ++i) {
				vr.toggleEndian((byte[]) items.get(i));
			}
		}
	}

	public final int length() {
		return items.isEmpty() ? 0 : -1;
	}

	public final boolean isEmpty() {
		return items.isEmpty();
	}

	public int vm(SpecificCharacterSet cs) {
		return items.isEmpty() ? 0 : 1;
	}

	public byte[] getBytes() {
		throw new UnsupportedOperationException();
	}

	public short getShort(boolean cache) {
		throw new UnsupportedOperationException();
	}

	public short[] getShorts(boolean cache) {
		throw new UnsupportedOperationException();
	}

	public int getInt(boolean cache) {
		throw new UnsupportedOperationException();
	}

	public int[] getInts(boolean cache) {
		throw new UnsupportedOperationException();
	}

	public float getFloat(boolean cache) {
		throw new UnsupportedOperationException();
	}

	public float[] getFloats(boolean cache) {
		throw new UnsupportedOperationException();
	}

	public double getDouble(boolean cache) {
		throw new UnsupportedOperationException();
	}

	public double[] getDoubles(boolean cache) {
		throw new UnsupportedOperationException();
	}

	public String getString(SpecificCharacterSet cs, boolean cache) {
		throw new UnsupportedOperationException();
	}

	public String[] getStrings(SpecificCharacterSet cs, boolean cache) {
		throw new UnsupportedOperationException();
	}

	public Date getDate(boolean cache) {
		throw new UnsupportedOperationException();
	}

	public Date[] getDates(boolean cache) {
		throw new UnsupportedOperationException();
	}

	public DateRange getDateRange(boolean cache) {
		throw new UnsupportedOperationException();
	}

	public Pattern getPattern(SpecificCharacterSet cs, boolean ignoreCase,
			boolean cache) {
		throw new UnsupportedOperationException();
	}

	public final boolean hasItems() {
		return true;
	}

	public final int countItems() {
		return items.size();
	}

	public final boolean hasDicomObjects() {
		return vr == VR.SQ;
	}

	public final boolean hasFragments() {
		return vr != VR.SQ;
	}

	public DicomObject getDicomObject() {
		return (DicomObject) (!items.isEmpty() ? items.get(0) : null);
	}

	public DicomObject getDicomObject(int index) {
		return (DicomObject) items.get(index);
	}

	public Object getObject(int index) {
		return items.get(index);
	}

	public DicomObject removeDicomObject(int index) {
		DicomObject ret = (DicomObject) items.remove(index);
		ret.setParent(null);
		updateItemPositions(index);
		return ret;
	}

	public boolean removeDicomObject(DicomObject item) {
		if (!items.remove(item)) {
			return false;
		}
		item.setParent(null);
		updateItemPositions(item.getItemPosition()-1);
		return true;
	}

	private void updateItemPositions(int index) {
		for (int i = index, n = countItems(); i < n; ++i) {
			getDicomObject(i).setItemPosition(i + 1);
		}
	}

	public DicomObject addDicomObject(DicomObject item) {
		if (vr != VR.SQ)
			throw new UnsupportedOperationException();
		if (item == null)
			throw new NullPointerException();
		items.add(item);
		item.setParent(parent);
		item.setItemPosition(countItems());
		return item;
	}

	public DicomObject addDicomObject(int index, DicomObject item) {
		if (vr != VR.SQ)
			throw new UnsupportedOperationException();
		if (item == null)
			throw new NullPointerException();
		items.add(index, item);
		item.setParent(parent);
		updateItemPositions(index);
		return item;
	}

	public DicomObject setDicomObject(int index, DicomObject item) {
		if (vr != VR.SQ)
			throw new UnsupportedOperationException();
		if (item == null)
			throw new NullPointerException();
		items.set(index, item);
		item.setParent(parent);
		item.setItemPosition(index + 1);
		return item;
	}

	public byte[] getFragment(int index) {
		return (byte[]) items.get(index);
	}

	public byte[] removeFragment(int index) {
		return (byte[]) items.remove(index);
	}

	public boolean removeFragment(byte[] b) {
		return items.remove(b);
	}

	public byte[] addFragment(byte[] b) {
		if (hasDicomObjects())
			throw new UnsupportedOperationException();
		if (b == null)
			throw new NullPointerException();
		items.add(b);
		return b;
	}

	public byte[] addFragment(int index, byte[] b) {
		if (hasDicomObjects())
			throw new UnsupportedOperationException();
		if (b == null)
			throw new NullPointerException();
		items.add(index, b);
		return b;
	}

	public byte[] setFragment(int index, byte[] b) {
		if (hasDicomObjects())
			throw new UnsupportedOperationException();
		if (b == null)
			throw new NullPointerException();
		items.set(index, b);
		return b;
	}

	public DicomElement filterItems(DicomObject filter) {
		if (!hasDicomObjects())
			throw new UnsupportedOperationException();
		if (filter == null)
			return this;
		int count = countItems();
		List<Object> tmp = new ArrayList<Object>(count);
		for (int i = 0; i < count; i++) {
			tmp.add(getDicomObject(i).subSet(filter));
		}
		return new SequenceDicomElement(tag, vr, bigEndian, tmp, parent);
	}

	public String getValueAsString(SpecificCharacterSet cs, int truncate) {
		throw new UnsupportedOperationException();
	}

}

