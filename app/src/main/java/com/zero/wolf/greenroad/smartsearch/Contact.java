package com.zero.wolf.greenroad.smartsearch;

/**
 *@author xiaobo.cui 2014年11月24日 下午5:36:29
 *
 */
public class Contact {


	public String mImgurl;
	public String mScientificname;
	public String mAlias;

	public Contact(String scientificname, String alias, String imgurl) {
		mScientificname = scientificname;
		mAlias = alias;
		mImgurl = imgurl;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mScientificname == null) ? 0 : mScientificname.hashCode());
		result = prime * result + ((mAlias == null) ? 0 : mAlias.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contact other = (Contact) obj;
		if (mScientificname == null) {
			if (other.mScientificname != null)
				return false;
		} else if (!mScientificname.equals(other.mScientificname))
			return false;
		if (mAlias == null) {
			if (other.mAlias != null)
				return false;
		} else if (!mAlias.equals(other.mAlias))
			return false;

		return true;
	}

}
