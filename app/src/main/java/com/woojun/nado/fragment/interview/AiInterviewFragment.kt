package com.woojun.nado.fragment.interview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.VideoResult
import com.otaliastudios.cameraview.controls.Facing
import com.otaliastudios.cameraview.controls.Mode
import com.woojun.nado.R
import com.woojun.nado.databinding.FragmentAiInterviewBinding
import com.woojun.nado.network.RetrofitAPI
import com.woojun.nado.network.RetrofitClient
import com.woojun.nado.util.Utils
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class AiInterviewFragment : Fragment() {

    private var _binding: FragmentAiInterviewBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAiInterviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.finishButton.visibility = View.INVISIBLE
        binding.camera.mode = Mode.VIDEO
        binding.camera.facing = Facing.FRONT

        binding.micButton.setOnClickListener {
            if (binding.camera.isTakingVideo) {
                binding.camera.stopVideo()
            } else if (binding.camera.mode == Mode.VIDEO) {
                binding.camera.takeVideo(
                    File(requireActivity().filesDir, "${System.currentTimeMillis()}.mp4")
                )
            }
        }

        binding.camera.setLifecycleOwner(this@AiInterviewFragment)
        binding.camera.addCameraListener(object : CameraListener() {
            override fun onPictureTaken(result: PictureResult) {
            }

            override fun onVideoTaken(result: VideoResult) {
                postAnalysisInterview(result.file)
            }

            override fun onVideoRecordingEnd() {
                binding.finishButton.visibility = View.VISIBLE
            }

            override fun onPictureShutter() {

            }

            override fun onVideoRecordingStart() {

            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun postAnalysisInterview(file: File) {
        val (loadingDialog, setDialogText) = Utils.createLoadingDialog(requireContext())
        loadingDialog.show()
        setDialogText("AI 분석 중")

        val requestBody = RequestBody.create(MediaType.parse("video/*"), file)
        val multipartBody = MultipartBody.Part.createFormData("file", file.name, requestBody)

        val retrofitAPI = RetrofitClient.getInstance().create(RetrofitAPI::class.java)
        val call: Call<String> = retrofitAPI.postAnalysisInterview(multipartBody)

        call.enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                if (response.isSuccessful) {
                    findNavController().apply {
                        popBackStack()
                        navigate(
                            R.id.aiInterviewResultFragment,
                            Bundle().apply {
                                this.putString("content", response.body().toString())
                            }
                        )
                    }
                } else {
                    Toast.makeText(requireContext(), "네트워크 오류, 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
                loadingDialog.dismiss()
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(requireContext(), "네트워크 오류, 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                loadingDialog.dismiss()
            }
        })
    }

}