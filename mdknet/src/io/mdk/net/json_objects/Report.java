package io.mdk.net.json_objects;

import static org.bytedeco.javacpp.helper.opencv_objdetect.cvHaarDetectObjects;
import static org.bytedeco.javacpp.opencv_core.CV_8UC1;
import static org.bytedeco.javacpp.opencv_core.cvClearMemStorage;
import static org.bytedeco.javacpp.opencv_core.cvGetSeqElem;
import static org.bytedeco.javacpp.opencv_core.cvLoad;
import static org.bytedeco.javacpp.opencv_core.cvMat;
import static org.bytedeco.javacpp.opencv_core.cvPoint;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvDecodeImage;
import static org.bytedeco.javacpp.opencv_imgproc.CV_AA;
import static org.bytedeco.javacpp.opencv_imgproc.cvRectangle;
import static org.bytedeco.javacpp.opencv_objdetect.CV_HAAR_DO_CANNY_PRUNING;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core.CvMemStorage;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.CvSeq;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacpp.opencv_objdetect.CvHaarClassifierCascade;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;

import com.google.gson.annotations.SerializedName;

public class Report {

	@SerializedName("ImageDat")
	protected byte[][] imgs;
	
	@SerializedName("face_detected")
	protected byte[] detected;
	
	@SerializedName("Gender")
	public boolean gender;
	
	@SerializedName("symptoms")
	public String[] truesymptoms;
	
	public int age;
	// TRUE = MALE, FALSE = FEMALE
	
	public void detect(String xml){
		Loader.load(opencv_objdetect.class);
		byte[] todet = imgs[0]; // The first image will go for facial recognition
		IplImage src = cvDecodeImage(cvMat(1, todet.length, CV_8UC1, new BytePointer(todet)));
		CvHaarClassifierCascade cascade = new CvHaarClassifierCascade(cvLoad(xml));
		CvMemStorage storage = CvMemStorage.create();
		CvSeq seq = cvHaarDetectObjects(src, cascade, storage, 2, 3, CV_HAAR_DO_CANNY_PRUNING);
		cvClearMemStorage(storage);
		int total = seq.total();
		for (int x = 0; x < total; x++){
			@SuppressWarnings("resource")
			CvRect rect = new CvRect(cvGetSeqElem(seq, x));
			cvRectangle(src, cvPoint(rect.x(), rect.y()), cvPoint(rect.x() + rect.width(), rect.y() + rect.height()), CvScalar.CYAN, 2, CV_AA, 0);
		}
		detected = IPLtobarr(src);
//		try {	// Debug
//			File f = new File("./test.png");
//			Files.write(Paths.get(f.getAbsolutePath()), detected);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	public static byte[] IPLtobarr(IplImage image){
		BufferedImage im = new Java2DFrameConverter().convert(new OpenCVFrameConverter.ToIplImage().convert(image));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] barr = null;
		try{
			ImageIO.write(im,"png",baos);
		    baos.flush();
		    barr = baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return barr;
	}
	
}
