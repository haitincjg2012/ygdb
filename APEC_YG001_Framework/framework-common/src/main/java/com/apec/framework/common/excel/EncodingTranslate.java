package com.apec.framework.common.excel;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Hashtable;



/*

 * Copyright 2002 Erik Peterson Code and program free for non-commercial use.

 * Contact erik@mandarintools.com for fees and licenses for commercial use.

 */


/**
 * @author xx
 */
public class EncodingTranslate extends Encoding {

	// Simplfied/Traditional character equivalence hashes

	protected static Hashtable<String, String> s2thash, t2shash;



	static {

		String dataline;



		// Initialize and load in the simplified/traditional character hashses

		s2thash = new Hashtable<>();

		t2shash = new Hashtable<>();



		try {

			InputStream pydata = EncodingTranslate.class

					.getResourceAsStream("hcutf8.txt");

			BufferedReader in = new BufferedReader(new InputStreamReader(

					pydata, "UTF8"));

			while ((dataline = in.readLine()) != null) {

				// Skip empty and commented lines

				if (dataline.length() == 0 || dataline.charAt(0) == '#') {

					continue;

				}



				// Simplified to Traditional, (one to many, but pick only one)

				s2thash.put(dataline.substring(0, 1).intern(), dataline

						.substring(1, 2));



				// Traditional to Simplified, (many to one)

				for (int i = 1; i < dataline.length(); i++) {

					t2shash.put(dataline.substring(i, i + 1).intern(), dataline

							.substring(0, 1));

				}
			}
			
			pydata.close();

		} catch (Exception e) {

			System.err.println(e);

		}



	}



	public static String convertString(String dataline, int sourceEncoding,

			int targetEncoding) {

		StringBuffer outline = new StringBuffer();

		int lineindex;



		if (sourceEncoding == HZ) {

			dataline = hz2gb(dataline);

		}

		for (lineindex = 0; lineindex < dataline.length(); lineindex++) {

			boolean flag = (sourceEncoding == GB2312 || sourceEncoding == GBK

					|| sourceEncoding == ISO2022CN_GB || sourceEncoding == HZ

					|| sourceEncoding == UNICODE

					|| sourceEncoding == UNICODES || sourceEncoding == UTF8)

					&& (targetEncoding == BIG5 || targetEncoding == CNS11643

					|| targetEncoding == UNICODET || targetEncoding == ISO2022CN_CNS);
			boolean flag1 = (sourceEncoding == BIG5 || sourceEncoding == CNS11643

					|| sourceEncoding == UNICODET || sourceEncoding == UTF8

					|| sourceEncoding == ISO2022CN_CNS

					|| sourceEncoding == GBK || sourceEncoding == UNICODE)

					&& (targetEncoding == GB2312

					|| targetEncoding == UNICODES

					|| targetEncoding == ISO2022CN_GB || targetEncoding == HZ);
			if (flag) {

				if (s2thash.containsKey(dataline.substring(lineindex,

						lineindex + 1)) == true) {

					outline.append(s2thash.get(dataline.substring(lineindex,

							lineindex + 1).intern()));

				} else {

					outline

							.append(dataline

									.substring(lineindex, lineindex + 1));

				}

			} else if (flag1) {

				if (t2shash.containsKey(dataline.substring(lineindex,

						lineindex + 1)) == true) {

					outline.append(t2shash.get(dataline.substring(lineindex,

							lineindex + 1).intern()));

				} else {

					outline

							.append(dataline

									.substring(lineindex, lineindex + 1));

				}

			} else {

				outline.append(dataline.substring(lineindex, lineindex + 1));

			}

		}



		if (targetEncoding == HZ) {

			// Convert to look like HZ

			return gb2hz(outline.toString());

		}



		return outline.toString();

	}



	public static String hz2gb(String hzstring) {

		byte[] hzbytes = new byte[2];

		byte[] gbchar = new byte[2];

		int byteindex = 0;

		StringBuffer gbstring = new StringBuffer("");



		try {

			hzbytes = hzstring.getBytes("8859_1");

		} catch (Exception usee) {

			System.err.println("Exception " + usee.toString());

			return hzstring;

		}



		// Convert to look like equivalent Unicode of GB

		for (byteindex = 0; byteindex < hzbytes.length; byteindex++) {

			if (hzbytes[byteindex] == 0x7e) {

				if (hzbytes[byteindex + 1] == 0x7b) {

					byteindex += 2;

					while (byteindex < hzbytes.length) {

						if (hzbytes[byteindex] == 0x7e

								&& hzbytes[byteindex + 1] == 0x7d) {

							byteindex++;

							break;

						} else if (hzbytes[byteindex] == 0x0a

								|| hzbytes[byteindex] == 0x0d) {

							gbstring.append((char) hzbytes[byteindex]);

							break;

						}

						gbchar[0] = (byte) (hzbytes[byteindex] + 0x80);

						gbchar[1] = (byte) (hzbytes[byteindex + 1] + 0x80);

						try {

							gbstring.append(new String(gbchar, "GB2312"));

						} catch (Exception usee) {

							System.err.println("Exception " + usee.toString());

						}

						byteindex += 2;

					}

				} else if (hzbytes[byteindex + 1] == 0x7e) {
					// ~~ becomes ~

					gbstring.append('~');

				} else { // false alarm

					gbstring.append((char) hzbytes[byteindex]);

				}

			} else {

				gbstring.append((char) hzbytes[byteindex]);

			}

		}

		return gbstring.toString();

	}



	public static String gb2hz(String gbstring) {

		StringBuffer hzbuffer;

		byte[] gbbytes = new byte[2];

		int i;

		boolean terminated = false;



		hzbuffer = new StringBuffer("");

		try {

			gbbytes = gbstring.getBytes("GB2312");

		} catch (Exception usee) {

			System.err.println(usee.toString());

			return gbstring;

		}



		for (i = 0; i < gbbytes.length; i++) {

			if (gbbytes[i] < 0) {

				hzbuffer.append("~{");

				terminated = false;

				while (i < gbbytes.length) {

					if (gbbytes[i] == 0x0a || gbbytes[i] == 0x0d) {

						hzbuffer.append("~}" + (char) gbbytes[i]);

						terminated = true;

						break;

					} else if (gbbytes[i] >= 0) {

						hzbuffer.append("~}" + (char) gbbytes[i]);

						terminated = true;

						break;

					}

					hzbuffer.append((char) (gbbytes[i] + 256 - 0x80));

					hzbuffer.append((char) (gbbytes[i + 1] + 256 - 0x80));

					i += 2;

				}

				if (terminated == false) {

					hzbuffer.append("~}");

				}

			} else {

				if (gbbytes[i] == 0x7e) {

					hzbuffer.append("~~");

				} else {

					hzbuffer.append((char) gbbytes[i]);

				}

			}

		}

		return new String(hzbuffer);

	}



	public static void convertFile(String sourcefile, String outfile,

			int sourceEncoding, int targetEncoding) {

		BufferedReader srcbuffer;

		BufferedWriter outbuffer;

		String dataline;



		try {

			srcbuffer = new BufferedReader(new InputStreamReader(

					new FileInputStream(sourcefile), javaname[sourceEncoding]));

			outbuffer = new BufferedWriter(new OutputStreamWriter(

					new FileOutputStream(outfile), javaname[targetEncoding]));

			while ((dataline = srcbuffer.readLine()) != null) {

				outbuffer.write(convertString(dataline, sourceEncoding,

						targetEncoding));

				outbuffer.newLine();

			}

			srcbuffer.close();

			outbuffer.close();

		} catch (Exception ex) {

			System.err.println(ex);

		}

	}
}