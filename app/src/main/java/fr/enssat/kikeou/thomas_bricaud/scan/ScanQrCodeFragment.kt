package fr.enssat.kikeou.thomas_bricaud.scan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import fr.enssat.kikeou.thomas_bricaud.KikeouApplication
import fr.enssat.kikeou.thomas_bricaud.MainActivity
import fr.enssat.kikeou.thomas_bricaud.R
import fr.enssat.kikeou.thomas_bricaud.database.*
import fr.enssat.kikeou.thomas_bricaud.databinding.FragmentScanQrCodeBinding
import fr.enssat.kikeou.thomas_bricaud.json.JsonObject
import java.util.*

class ScanQrCodeFragment : Fragment() {
    //binding information
    private lateinit var binding: FragmentScanQrCodeBinding
    private lateinit var viewModel: ScanQrCodeModel
    private lateinit var viewModelFactory: ScanQrCodeModelFactory

    // database repository
    private lateinit var personRepository: PersonRepository
    private lateinit var weekRepository: WeekRepository

    private val contract = CameraActivityContract()
    private val getQrCode = registerForActivityResult(contract) { json: String? ->
        if(json != null) {
            print(json)
            val moshi: Moshi = Moshi.Builder().build()
            val jsonAdapter: JsonAdapter<JsonObject> = moshi.adapter(JsonObject::class.java)
            val jsonObject = jsonAdapter.fromJson(json)
            if(jsonObject != null) {
                var person = jsonObject.getPerson()
                val week = jsonObject.getWeek()


                val persons = personRepository.getPerson(person.name)
                if (persons.isNotEmpty()) {
                    personRepository.delete(person.name)
                }

                personRepository.insert(person)

                val weeks = weekRepository.getWeek(week.num, week.name)

                if (weeks.isNotEmpty()) {
                    weekRepository.delete(week.num, week.name)
                }
                weekRepository.insert(week)


                // setter on initialization
                binding.person.name.text = person.name
                binding.person.email.text = person.contact.mail
                binding.person.phone.text = person.contact.tel

                binding.banner.spinner.text = week.num.toString();

                binding.week.monday.text = week.day1
                binding.week.tuesday.text = week.day2
                binding.week.wednesday.text = week.day3
                binding.week.thursday.text = week.day4
                binding.week.friday.text = week.day5
                binding.week.saturday.text = week.day6
            }
            // Save in db the json
        } else {
            print("No Json detected")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getQrCode.launch(null)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentScanQrCodeBinding.inflate(inflater, container, false)

        // integrate viewmodel for databinding
        viewModelFactory = ScanQrCodeModelFactory(((context as MainActivity).application as KikeouApplication),"", "", "", "", "", "", "", "", "", "")
        viewModel = ViewModelProvider(this, viewModelFactory).get(ScanQrCodeModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner

        // setter on initialization
        binding.person.name.text = viewModel.name
        binding.person.email.text = viewModel.email
        binding.person.phone.text = viewModel.phone

        binding.banner.spinner.text = viewModel.week
        binding.week.monday.text = viewModel.monday
        binding.week.tuesday.text = viewModel.tuesday
        binding.week.wednesday.text = viewModel.wednesday
        binding.week.thursday.text = viewModel.thursday
        binding.week.friday.text = viewModel.friday
        binding.week.saturday.text = viewModel.saturday

        personRepository = viewModel.personRepository
        weekRepository = viewModel.weekRepository

        return binding.root
    }
}