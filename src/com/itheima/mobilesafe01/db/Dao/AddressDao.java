package com.itheima.mobilesafe01.db.Dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AddressDao {
	/**
	 * �����ز�ѯ����
	 * 
	 */
	private static final String path = "data/data/com.itheima.mobilesafe01/files/address.db";

	public static String getAddress(String number) {
		String address = "δ֪����";
		SQLiteDatabase database = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		// �ֻ������ص�: 1 + (3,4,5,6,7,8) + (9λ����)
		// ������ʽ
		// ^1[3-8]\d{9}$

		if (number.matches("^1[3-8]\\d{9}$")) {// ƥ���ֻ�����
			Cursor cursor = database
					.rawQuery(
							"select location from data2 where id=(select outkey from data1 where id=?)",
							new String[] { number.substring(0, 7) });

			if (cursor.moveToNext()) {
				address = cursor.getString(0);
			}

			cursor.close();
		} else if (number.matches("^\\d+$")) {// ƥ������
			switch (number.length()) {
			case 3:
				address = "�����绰";
				break;
			case 4:
				address = "ģ����";
				break;
			case 5:
				address = "�ͷ��绰";
				break;
			case 7:
			case 8:
				address = "���ص绰";
				break;
			default:
				break;
			}
		}
		return address;
	}
}
