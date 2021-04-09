package com.example.appempleado.cargarMenu

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.appempleado.R
import com.example.appempleado.databinding.FragmentCargarMenuBinding
import com.google.firebase.auth.FirebaseAuth


class FragmentCargarMenu : Fragment() {
    private var _binding: FragmentCargarMenuBinding? = null
    private val binding get() = _binding!!



    fun Context.vibrate(milliseconds:Long = 15){
        val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val canVibrate:Boolean = vibrator.hasVibrator()
        if(canVibrate){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        milliseconds,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            }else{
                vibrator.vibrate(milliseconds)
            }
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return super.onContextItemSelected(item)
        //Toast.makeText(context, "Saliendo ", Toast.LENGTH_SHORT).show()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCargarMenuBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.btnAltaPlato.setOnClickListener{ altaPlato(0)}
        setHasOptionsMenu(true)
        return view
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu, inflater:MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        FirebaseAuth.getInstance().signOut()
        findNavController().navigate(R.id.action_fragmentCargarMenu_to_fragmentPortada,null)
        return true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var viewPager: ViewPager =  requireActivity().findViewById<View>(R.id.viewPager) as ViewPager
        viewPager.adapter = CustomPagerAdapter(requireContext())
        viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) { }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                context?.vibrate()
                binding.btnAltaPlato.setOnClickListener{ altaPlato(position)}
            }
        })
    }

    private fun logout(){
        FirebaseAuth.getInstance().signOut()
        findNavController().navigate(R.id.action_fragmentCargarMenu_to_fragmentHome,null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun altaPlato(numeroMenu:Int){
        var intent: Intent? = null
        when(numeroMenu){
            0 -> intent = Intent(context, MenuDelDiaActivity::class.java)
            1 -> intent = Intent(context, MenuSemanalActivity::class.java)
            2 -> intent = Intent(context, MenuMeriendaActivity::class.java)
        }
        startActivity(intent)
    }
}


class CustomPagerAdapter(private val mContext: Context) : PagerAdapter() {

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val modelObject = Menu.values()[position]
        val inflater = LayoutInflater.from(mContext)
        val layout = inflater.inflate(modelObject.layoutResId, collection, false) as ViewGroup
        collection.addView(layout)
        return layout
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    override fun getCount(): Int {
        return Menu.values().size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getPageTitle(position: Int): CharSequence {
        val customPagerEnum = Menu.values()[position]
        return mContext.getString(customPagerEnum.titleResId)
    }
}


enum class Menu private constructor(val titleResId: Int, val layoutResId: Int) {
    MENU1(1, R.layout.item_menu_del_dia),
    MENU2(2, R.layout.item_menu_semanal),
    MENU3(3, R.layout.item_menu_desayuno_merienda)
}




